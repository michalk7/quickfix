package com.kubara.michal.inzynierka.core.validation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import com.google.common.base.Joiner;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		URL resource = this.getClass().getClassLoader().getResource("messages.properties");
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(resource.getPath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		MessageResolver resolver = new PropertiesMessageResolver(props);
		
		PasswordValidator validator = new PasswordValidator(
					resolver,
					new LengthRule(8, 30), 
					new CharacterRule(EnglishCharacterData.UpperCase , 1), 
					new CharacterRule(EnglishCharacterData.Digit, 1), 
					new CharacterRule(EnglishCharacterData.Special, 1), 
					new WhitespaceRule());
 
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(Joiner.on("|;|").join(validator.getMessages(result))).addConstraintViolation();
        return false;
	}

}
