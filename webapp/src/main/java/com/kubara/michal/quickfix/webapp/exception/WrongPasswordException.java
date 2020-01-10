package com.kubara.michal.quickfix.webapp.exception;


@SuppressWarnings("serial")
public class WrongPasswordException extends Throwable {

	public WrongPasswordException(final String message) {
		super(message);
	}
	
}
