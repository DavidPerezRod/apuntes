package com.zerti.utilities.iban.exception;

public class IBANIncorrectException extends Exception{

	private static final long serialVersionUID = -7053690197822769908L;

	public IBANIncorrectException(String iban) {
		super(String.format("IBAN %s incorrecto", iban));
	}

}
