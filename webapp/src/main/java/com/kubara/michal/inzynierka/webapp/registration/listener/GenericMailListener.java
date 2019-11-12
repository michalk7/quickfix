package com.kubara.michal.inzynierka.webapp.registration.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;

import com.kubara.michal.inzynierka.webapp.registration.GenericMailEvent;

public class GenericMailListener implements ApplicationListener<GenericMailEvent> {

	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void onApplicationEvent(GenericMailEvent event) {
		this.sendMail(event);
	}
	
	private void sendMail(GenericMailEvent event) {
		mailSender.send(event.getMail());
	}

}
