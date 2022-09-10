package com.zerti.utilities.iban.dominio;

import com.zerti.utilities.iban.exception.IBANIncorrectException;

public class BelgianIBAN extends IBAN {
	
	private static final int MIN_LENGTH = 7;
	private static final int MAX_LENGTH = 16;

	public IBAN getIBANFiltered(String iban) throws IBANIncorrectException {
		return super.getIBANFiltered(iban,MIN_LENGTH, MAX_LENGTH);
	}

	@Override
	IBAN getIBANMinimun(String iban) {
		this.setCountryCode(iban.substring(0, 2));
		this.setDc(iban.substring(2, 4));
		this.setBankCode(iban.substring(4, 7));
		this.setIban(iban);

		return this;
	}

	@Override
	IBAN getIBANMaximun(String iban) {
		getIBANMinimun(iban);
		this.setAccountNumber(iban.substring(7, 14));
		this.setControlNumber(iban.substring(14, 16));
		return this;
	}

}
