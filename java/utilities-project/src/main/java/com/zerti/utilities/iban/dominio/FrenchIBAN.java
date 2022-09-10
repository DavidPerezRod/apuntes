package com.zerti.utilities.iban.dominio;

import com.zerti.utilities.iban.exception.IBANIncorrectException;

public class FrenchIBAN extends IBAN {
	
	private static final int MIN_LENGTH = 9;
	private static final int MAX_LENGTH = 27;
	
	public IBAN getIBANFiltered(String iban) throws IBANIncorrectException {
		return super.getIBANFiltered(iban, MIN_LENGTH, MAX_LENGTH);
	}

	@Override
	IBAN getIBANMinimun(String iban) {
		this.setCountryCode(iban.substring(0, 2));
		this.setDc(iban.substring(2, 4));
		this.setBankCode(iban.substring(4, 9));
		this.setIban(iban);
		return this;
	}

	@Override
	IBAN getIBANMaximun(String iban) {
		getIBANMinimun(iban);
		this.setOfficeCode(iban.substring(9, 14));
		this.setAccountNumber(iban.substring(14, 25));
		this.setControlNumber(iban.substring(25));
		return this;
	}
	
}
