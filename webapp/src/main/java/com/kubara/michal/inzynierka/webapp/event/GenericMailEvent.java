package com.kubara.michal.inzynierka.webapp.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.mail.SimpleMailMessage;

import com.kubara.michal.inzynierka.core.entity.User;

@SuppressWarnings("serial")
public class GenericMailEvent extends ApplicationEvent {
	
	private SimpleMailMessage mail;
	private User user;
	
	public GenericMailEvent(User user, SimpleMailMessage mail) {
		super(user);
		this.mail = mail;
		this.user = user;
	}

	public SimpleMailMessage getMail() {
		return mail;
	}

	public void setMail(SimpleMailMessage mail) {
		this.mail = mail;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
