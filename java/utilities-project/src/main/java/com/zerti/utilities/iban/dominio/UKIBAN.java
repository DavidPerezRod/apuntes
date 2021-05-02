package com.zerti.utilities.iban.dominio;

import com.zerti.utilities.iban.exception.IBANIncorrectException;

public class UKIBAN extends IBAN {

	private static final int MIN_LENGTH = 8;
	private static final int MAX_LENGTH = 22;
	
	public IBAN getIBANFiltered(String iban) throws IBANIncorrectException {
		return super.getIBANFiltered(iban, MIN_LENGTH, MAX_LENGTH);
	}

	@Override
	IBAN getIBANMinimun(String iban) {
		this.setCountryCode(iban.substring(0, 2));
		this.setDc(iban.substring(2, 4));
		this.setBankCode(iban.substring(4, 8));
		this.setIban(iban);
		return this;
	}

	@Override
	IBAN getIBANMaximun(String iban) {
		getIBANMinimun(iban);
		this.setTypeAccountCode(iban.substring(8, 14));
		this.setAccountNumber(iban.substring(14));
		return this;
	}
	
}
