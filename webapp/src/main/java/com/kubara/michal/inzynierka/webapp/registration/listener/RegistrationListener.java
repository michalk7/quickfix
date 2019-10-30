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
		String subject = "QuickFix - Potwierdzenie Rejestracji";
		String confirmationUrl = event.getAppUrl() + "/register/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.confirmMailText", null, event.getLocale());
        String signature = messages.getMessage("message.confirmMailSignature", null, event.getLocale());
         
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "http://localhost:8080" + confirmationUrl + signature);
        //email.setFrom("jankwtest@wp.pl");
        email.setFrom(env.getProperty("spring.mail.username"));
        //email.setFrom(new InternetAddress("jankwtest@wp.pl", "no-reply@quickfix.com"));
        mailSender.send(email);

		//System.out.println("Mail wysłany");
		
	}

}
