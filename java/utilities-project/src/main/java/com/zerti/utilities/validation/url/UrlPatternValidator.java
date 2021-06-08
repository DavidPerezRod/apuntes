package com.zerti.utilities.validation.url;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlPatternValidator implements ConstraintValidator<ValidUrl, String> {

	private static final String URL_REGEX = "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))"
			+ "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" + "([).!';/?:,][[:blank:]])?$";

	private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
	
	private boolean required;
	
	@Override
	public void initialize(ValidUrl annotation) {
		this.required = annotation.required();
	}

	private boolean urlValidator(String url) {
		if (url == null) {
			return false;
		}

		Matcher matcher = URL_PATTERN.matcher(url);
		return matcher.matches();
	}

	@Override
	public boolean isValid(String url, ConstraintValidatorContext context) {
		return !required || (urlValidator(url) && required);
	}

}
