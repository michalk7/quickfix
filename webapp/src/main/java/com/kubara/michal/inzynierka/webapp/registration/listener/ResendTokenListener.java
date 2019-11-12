package com.kubara.michal.inzynierka.webapp.registration.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.kubara.michal.inzynierka.webapp.registration.OnTokenResendEvent;

@Component
public class ResendTokenListener implements ApplicationListener<OnTokenResendEvent> {

	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void onApplicationEvent(OnTokenResendEvent event) {
		this.sendNewToken(event);
	}

	private void sendNewToken(OnTokenResendEvent event) {
		mailSender.send(event.getMail());
	}

}
