package com.myip.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.myip.exception.IpNotFoundException;

public class Connection {
	
	private RequestHeader header;
	private StringBuilder content;
	private int code;
	
	private boolean initialized;
	
	public static final String PATTERN = "((?:(2[0-4][0-9])|(2[0-5][0-5])|(1[0-9][0-9])|([0-9][0-9])|([0-9]))\\.)((?:(2[0-4][0-9])|(2[0-5][0-5])|(1[0-9][0-9])|([0-9][0-9])|([0-9]))\\.)((?:(2[0-4][0-9])|(2[0-4][0-9])|(2[0-5][0-5])|(1[0-9][0-9])|([0-9][0-9])|([0-9]))\\.)((?:(2[0-4][0-9])|(2[0-5][0-5])|(1[0-9][0-9])|([0-9][0-9])|([0-9])))";
	
	private static final String URL = "http://checkip.amazonaws.com";

	public Connection() {
		this.header = new RequestHeader();
		this.content = new StringBuilder(15);
	}
	
	public String getIp() {
		if(initialized) {
			if(this.content.toString().isEmpty())
				return null;
			else
				return this.content.toString();
		}else
			throw new RuntimeException("Not initialized connection");
	}
	
	public int getCode() {
		if(initialized) {
			return this.code;
		}else
			throw new RuntimeException("Not initialized connection");
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
	
	public void requestPage() throws IpNotFoundException{
		URL url = null;
		
		if(initialized) {
			throw new RuntimeException("Connection already used");
		}
		
		try {
			url = new URL(URL);
		}catch(MalformedURLException ex) { }
		
		if(url != null) {
			try {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				
				connection.setConnectTimeout(10000);
				connection.setReadTimeout(10000);
				
				Map<String, String> headerProperties = this.header.getHeaderProperies();
				
				for(Map.Entry<String, String> entry : headerProperties.entrySet())
					connection.setRequestProperty(entry.getKey(), entry.getValue());

				this.code = connection.getResponseCode();
				
				initialized = true;
				
				if(this.code >= 200 || this.code < 400) {
					try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))){
						this.content.append(reader.readLine().trim());
					}
					
					if(!this.content.toString().matches(PATTERN)) {
						this.content.delete(0, this.content.length());
						throw new IpNotFoundException();
					}
				}else
					throw new IpNotFoundException();
			}catch(IOException ex) { 
				throw new IpNotFoundException();
			}
		}
		
	}
	


}
