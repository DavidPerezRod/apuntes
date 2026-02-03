package com.zerti.utilities.validation.iban;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IBANValidator implements ConstraintValidator<ValidIBAN, String> {

	@Override
	public boolean isValid(String iban, ConstraintValidatorContext context) {
		org.apache.commons.validator.routines.IBANValidator validator = new org.apache.commons.validator.routines.IBANValidator();
		return validator.isValid(iban);
	}

}
