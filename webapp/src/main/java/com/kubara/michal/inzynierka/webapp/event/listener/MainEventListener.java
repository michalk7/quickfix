package com.kubara.michal.inzynierka.webapp.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.kubara.michal.inzynierka.webapp.event.GenericMailEvent;

@Component
public class MainEventListener {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	@EventListener
	public void sendGenericMail(GenericMailEvent event) {
		mailSender.send(event.getMail());
	}
	
}
