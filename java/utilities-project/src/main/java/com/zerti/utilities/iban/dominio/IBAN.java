package com.zerti.utilities.iban.dominio;

import com.zerti.utilities.iban.exception.IBANIncorrectException;
import org.apache.commons.validator.routines.IBANValidator;

public abstract class IBAN {
	
	private String ibanStr;
	
	private String countryCode;
	
	private String dc;
	
	private String bankCode;
	
	private String accountNumber;
	
	private String controlNumber;
	
	private String officeCode;
	
	private String typeAccountCode;
	
	private String swiftCode;

	public abstract IBAN getIBANFiltered(String iban) throws IBANIncorrectException ;

	abstract IBAN getIBANMinimun(String iban);

	abstract IBAN getIBANMaximun(String iban);

	public IBAN getIBANFiltered(String iban, int min_lenght, int max_length) throws IBANIncorrectException {
		IBANValidator validator = new IBANValidator();
		int length = iban.length();
		if(length < min_lenght) {
			throw new IBANIncorrectException(iban);
		} else if(length < max_length) {
			return getIBANMinimun(iban);
		}else if(validator.isValid(iban)) {
			return getIBANMaximun(iban);
		} else {
			throw new IBANIncorrectException(iban);
		}
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getDc() {
		return dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getControlNumber() {
		return controlNumber;
	}

	public void setControlNumber(String controlNumber) {
		this.controlNumber = controlNumber;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getTypeAccountCode() {
		return typeAccountCode;
	}

	public void setTypeAccountCode(String typeAccountCode) {
		this.typeAccountCode = typeAccountCode;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}


	public String getIban() {
		return ibanStr;
	}


	public void setIban(String iban) {
		this.ibanStr = iban;
	}
	
}
