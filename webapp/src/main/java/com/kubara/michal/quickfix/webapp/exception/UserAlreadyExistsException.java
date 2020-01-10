package com.kubara.michal.quickfix.webapp.exception;

@SuppressWarnings("serial")
public class UserAlreadyExistsException extends Throwable {
	
	private boolean emailException;

	public UserAlreadyExistsException(final String message, boolean emailException) {
		super(message);
		this.emailException = emailException;
	}

	public boolean isEmailException() {
		return emailException;
	}

	public void setEmailException(boolean emailException) {
		this.emailException = emailException;
	}
	
	
	
}
