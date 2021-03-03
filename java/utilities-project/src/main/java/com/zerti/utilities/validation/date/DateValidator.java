package com.zerti.utilities.validation.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<ValidDate, String> {
	

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Date today = new Date();
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			today = sdf.parse(sdf.format(today));
		    date = sdf.parse(value);
		} catch (ParseException ex) {
		    ex.printStackTrace();
		    return false;
		}
		if (date == null) {
		    return false;
		} else {
		    if(date.before(today)) {
		    	return false;
		    }
		}
		return true;
	}
}
