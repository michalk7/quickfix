package com.kubara.michal.inzynierka.webapp.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Constraint(validatedBy = CheckboxValidator.class)
public @interface IsCheckboxCheck {

	String message() default "Checkbox must be checked";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
}
