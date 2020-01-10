package com.kubara.michal.quickfix.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckboxValidator implements ConstraintValidator<IsCheckboxCheck, Boolean>{

	@Override
	public boolean isValid(final Boolean value, final ConstraintValidatorContext context) {
		return value != null && value.booleanValue();
	}

}
