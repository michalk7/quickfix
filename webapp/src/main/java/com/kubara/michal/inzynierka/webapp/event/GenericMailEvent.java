package com.kubara.michal.inzynierka.webapp.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.mail.SimpleMailMessage;


@SuppressWarnings("serial")
public class GenericMailEvent extends ApplicationEvent {
	
	private SimpleMailMessage mail;
	
	public GenericMailEvent(Object obj, SimpleMailMessage mail) {
		super(obj);
		this.mail = mail;
	}

	public SimpleMailMessage getMail() {
		return mail;
	}

	public void setMail(SimpleMailMessage mail) {
		this.mail = mail;
	}
	
}
