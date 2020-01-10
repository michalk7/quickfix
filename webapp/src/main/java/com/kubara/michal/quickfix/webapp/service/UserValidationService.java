package com.kubara.michal.quickfix.webapp.service;

public interface UserValidationService {
	
	String validateResetPasswordToken(long id, String token);

}
