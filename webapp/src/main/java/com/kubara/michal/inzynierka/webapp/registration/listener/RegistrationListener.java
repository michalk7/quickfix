package com.kubara.michal.inzynierka.webapp.registration.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.webapp.registration.OnRegistrationCompleteEvent;
import com.kubara.michal.inzynierka.webapp.service.UserService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private UserService service;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	Environment env;
	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		
		service.createVerificationToken(user, token);
		
		String recipientAddress = user.getEmail();
		String subject = "Potwierdzenie Rejestracji";
		String confirmationUrl = event.getAppUrl() + "/register/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLocale());
         
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " rn" + "http://localhost:8080" + confirmationUrl);
        email.setFrom("jankwtest@wp.pl");
        mailSender.send(email);

		System.out.println("Mail wysłany");
		
	}

}
