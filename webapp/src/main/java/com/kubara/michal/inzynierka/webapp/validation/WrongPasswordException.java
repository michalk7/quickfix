package com.kubara.michal.inzynierka.webapp.validation;


@SuppressWarnings("serial")
public class WrongPasswordException extends Throwable {

	public WrongPasswordException(final String message) {
		super(message);
	}
	
}
