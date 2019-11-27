package com.kubara.michal.inzynierka.core.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PolishPhoneNumberValidator implements ConstraintValidator<ValidPolishPhoneNumber, String>{

	private Pattern pattern;
	private Matcher matcher;
	private static final String POLISH_PHONE_NUMBER_PATTERN = "(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)";
	
	@Override
	public boolean isValid(final String phoneNumber, final ConstraintValidatorContext context) {
		pattern = Pattern.compile(POLISH_PHONE_NUMBER_PATTERN);
		if(phoneNumber == null) {
			return false;
		}
		
		matcher = pattern.matcher(phoneNumber);
		
		return matcher.matches();
	}

	
	
}
