package com.kubara.michal.inzynierka.core.validation;


@SuppressWarnings("serial")
public class WrongPasswordException extends Throwable {

	public WrongPasswordException(final String message) {
		super(message);
	}
	
}
