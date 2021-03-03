package com.zerti.utilities.iban.impl;

import java.util.Map;

import com.zerti.utilities.iban.dominio.BelgianIBAN;
import com.zerti.utilities.iban.dominio.FrenchIBAN;
import com.zerti.utilities.iban.dominio.IBAN;
import com.zerti.utilities.iban.dominio.IrishIBAN;
import com.zerti.utilities.iban.dominio.SpanishIBAN;
import com.zerti.utilities.iban.dominio.UKIBAN;
import com.zerti.utilities.iban.exception.IBANIncorrectException;

public class IBANFilterImpl {
	
	
	public IBANFilterImpl() {
		super();
	}
	
	private final Map<String, IBAN> mapIBAN = Map.of(
			"ES", new SpanishIBAN(),
			"BE", new BelgianIBAN(),
			"FR", new FrenchIBAN(),
			"GB", new UKIBAN(),
			"IE", new IrishIBAN()
			);
	
	public IBAN getIBANFiltered(String iban) throws IBANIncorrectException {
		String countryCode = iban.substring(0, 2).toUpperCase().replaceAll("\\s+","");
		return mapIBAN.get(countryCode).getIBANFiltered(iban);		
	}

}
