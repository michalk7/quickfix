package com.kubara.michal.quickfix.adminpanel.exception;

@SuppressWarnings("serial")
public class AdminAlreadyExistsException extends Throwable {
	
	private boolean emailException;

	public AdminAlreadyExistsException(final String message, boolean emailException) {
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
