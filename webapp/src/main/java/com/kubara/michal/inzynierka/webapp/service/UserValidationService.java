package com.kubara.michal.inzynierka.webapp.service;

public interface UserValidationService {
	
	String validateResetPasswordToken(long id, String token);

}
