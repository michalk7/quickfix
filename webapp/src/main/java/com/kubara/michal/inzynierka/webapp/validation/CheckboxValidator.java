package com.kubara.michal.inzynierka.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckboxValidator implements ConstraintValidator<IsCheckboxCheck, Boolean>{

	@Override
	public boolean isValid(final Boolean value, final ConstraintValidatorContext context) {
		return value != null && value.booleanValue();
	}

}