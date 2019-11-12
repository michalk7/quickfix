package com.kubara.michal.inzynierka.webapp.event;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;
import org.springframework.mail.SimpleMailMessage;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.core.entity.VerificationToken;

@SuppressWarnings("serial")
public class OnTokenResendEvent extends ApplicationEvent {

	private String appUrl;
    private Locale locale;
    private User user;
    private VerificationToken newToken;
    private SimpleMailMessage mail;
    
	public OnTokenResendEvent(User user, Locale locale, String appUrl , VerificationToken newToken, SimpleMailMessage mail) {
		super(user);
		this.appUrl = appUrl;
		this.locale = locale;
		this.user = user;
		this.newToken = newToken;
		this.mail = mail;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public VerificationToken getNewToken() {
		return newToken;
	}

	public void setNewToken(VerificationToken newToken) {
		this.newToken = newToken;
	}

	public SimpleMailMessage getMail() {
		return mail;
	}

	public void setMail(SimpleMailMessage mail) {
		this.mail = mail;
	}
   
}
