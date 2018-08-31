package com.myip.exception;

public class IpNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public IpNotFoundException() {
		super("Errore non previsto: Impossibile trovare l'indirizzo IP");
	}

}
