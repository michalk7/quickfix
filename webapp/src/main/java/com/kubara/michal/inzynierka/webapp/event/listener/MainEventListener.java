package com.kubara.michal.inzynierka.webapp.event.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.webapp.event.GenericMailEvent;
import com.kubara.michal.inzynierka.webapp.event.OnRegistrationCompleteEvent;
import com.kubara.michal.inzynierka.webapp.event.OnTokenResendEvent;
import com.kubara.michal.inzynierka.webapp.service.UserService;

@Component
public class MainEventListener {

	@Autowired
	private UserService service;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private Environment env;
	
	@Async
	@EventListener
	public void sendGenericMail(GenericMailEvent event) {
		mailSender.send(event.getMail());
	}
	
	@Async
	@EventListener
	public void confirmRegistration(OnRegistrationCompleteEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		
		service.createVerificationToken(user, token);
		
		String recipientAddress = user.getEmail();
		String subject = "QuickFix - Potwierdzenie Rejestracji";
		String confirmationUrl = event.getAppUrl() + "/register/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.confirmMailText", null, event.getLocale());
        String signature = messages.getMessage("message.confirmMailSignature", null, event.getLocale());
         
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "http://localhost:8080" + confirmationUrl + signature);
        email.setFrom(env.getProperty("spring.mail.username"));
        mailSender.send(email);

	}
	
	@Async
	@EventListener
	public void sendNewToken(OnTokenResendEvent event) {
		mailSender.send(event.getMail());
	}
	
	
}
