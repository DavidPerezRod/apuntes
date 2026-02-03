package com.zerti.utilities.iban.dominio;

import com.zerti.utilities.iban.exception.IBANIncorrectException;

public class IrishIBAN extends IBAN {
	
	private static final int MIN_LENGTH = 14;
	private static final int MAX_LENGTH = 22;
	

	public IBAN getIBANFiltered(String iban) throws IBANIncorrectException {
		return super.getIBANFiltered(iban, MIN_LENGTH, MAX_LENGTH);
	}

	@Override
	IBAN getIBANMinimun(String iban) {
		this.setCountryCode(iban.substring(0, 2));
		this.setDc(iban.substring(2, 4));
		this.setSwiftCode(iban.substring(4, 8));
		this.setBankCode(iban.substring(8, 14));
		this.setIban(iban);
		return this;
	}

	@Override
	IBAN getIBANMaximun(String iban) {
		getIBANMinimun(iban);
		this.setAccountNumber(iban.substring(14));
		return this;
	}

}
