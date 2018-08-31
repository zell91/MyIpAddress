package com.myip.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.myip.connection.Connection;
import com.myip.exception.IpNotFoundException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MyIPController implements Initializable {
	
	@FXML
	private BorderPane root;
	
	@FXML
	private Label stateLbl;
	
	@FXML
	private TextField ipText;
	
	@FXML	
	private VBox centerSection;
	
	@FXML
	private HBox buttonBar;
	
	@FXML
	private Button actionBtn, copyBtn;
	
	@Override
	public void initialize(URL fxmlURL, ResourceBundle resource) {	
		this.ipText.selectedTextProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				ipText.deselect();
				ipText.selectedTextProperty().removeListener(this);
			}			
		});
		this.ipText.setPromptText("0.0.0.0");
	}
	
	@FXML
	public void getMyIP(ActionEvent event) {
		Connection connection = new Connection();
		
		this.stateLbl.setText("Ricerca ip in corso...");
		this.ipText.setDisable(true);
		this.actionBtn.setDisable(true);
		
		Thread thread = new Thread(()->{
			try {
				connection.requestPage();
				String ip = connection.getIp();

				if(ip == null || !ip.matches(Connection.PATTERN)) throw new IpNotFoundException();
				
				this.ipText.setDisable(false);			
				this.copyBtn.setDisable(false);

				Platform.runLater(()->{
					this.ipText.setText(ip);
					this.stateLbl.setText("");
					this.copyBtn.requestFocus();
				});

			} catch (IpNotFoundException e) {
				Platform.runLater(()->{
					this.ipText.setText("IP NOT FOUND");
					this.stateLbl.setText("Impossibile trovere l'indirizzo ip");
				});
				this.copyBtn.setDisable(true);
				this.actionBtn.setDisable(false);
			}
		});
		
		thread.setDaemon(true);
		thread.start();
	}
	
	@FXML
	public void copy(ActionEvent event) {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		
		Map<DataFormat, Object> content = new HashMap<>();
		
		content.put(DataFormat.PLAIN_TEXT, this.ipText.getText());
		
		clipboard.setContent(content);
		this.stateLbl.setText("Copiato");
	}

}
