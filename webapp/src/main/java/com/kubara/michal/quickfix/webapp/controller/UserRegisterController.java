package com.kubara.michal.quickfix.webapp.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.kubara.michal.quickfix.core.entity.Category;
import com.kubara.michal.quickfix.core.entity.User;
import com.kubara.michal.quickfix.core.entity.VerificationToken;
import com.kubara.michal.quickfix.webapp.dto.ExpertDTO;
import com.kubara.michal.quickfix.webapp.dto.UserDTO;
import com.kubara.michal.quickfix.webapp.event.GenericMailEvent;
import com.kubara.michal.quickfix.webapp.exception.UserAlreadyExistsException;
import com.kubara.michal.quickfix.webapp.service.CategoryService;
import com.kubara.michal.quickfix.webapp.service.UserService;
import com.kubara.michal.quickfix.webapp.util.GenericResponse;

@Controller
@RequestMapping("/register")
public class UserRegisterController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private Environment env;
	
	Logger logger = LoggerFactory.getLogger(UserRegisterController.class);
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
	
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/showRegisterPage")
	public String showUserRegisterPage(Model model) {
		model.addAttribute("user", new UserDTO());
		model.addAttribute("validated", false);
		
		return "/user/user-registration-form";
	}
	
	
	@ModelAttribute("allCategoriesMultiCheckbox")
	public List<Category> getAllCategoriesMultiCheckboxValues() {
		return categoryService.findAll();
	}
	
	
	@PostMapping("/processExpertRegistrationForm")
	public String proccessExpertRegistrationForm(@Valid @ModelAttribute("expert") ExpertDTO dtoExpert, 
			BindingResult bindingResult, Model model, HttpServletRequest request) {
		
		if(bindingResult.hasErrors()) {
			return "/expert/expert-registration-form";
		}
		
		if(dtoExpert.getSelectedCategoriesFromCheckboxes().size() > 4 || dtoExpert.getSelectedCategoriesFromCheckboxes().size() < 1) {
			return "/expert/expert-registration-form";
		}
		
		String reversedUserName = reverseString(dtoExpert.getUserName());
		
		String newPass = dtoExpert.getPassword();
		
		if( newPass.contains(dtoExpert.getUserName()) || newPass.contains(reversedUserName)) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "/expert/expert-registration-form";
		}
		
		User registered = createNewExpertAccount(dtoExpert, bindingResult);
		if(registered == null) {
			return "/expert/expert-registration-form";
		}
		
		String token = UUID.randomUUID().toString();
		userService.createVerificationToken(registered, token);
		SimpleMailMessage mail = constructAccountCreatedEmail(getAppUrl(request), request.getLocale(), token, registered);
		
		try {
			logger.info("Przed odpaleniem listenera");
			eventPublisher.publishEvent(new GenericMailEvent(this, mail));
			logger.info("Po odpaleniu listenera");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("registrationError", "Niepowodzenie wysyłania linku aktywacyjnego");
			
			userService.delete(registered);
			
			return "/expert/expert-registration-form";
		}
		logger.info("Teraz redirect");
		
		return "redirect:/showLoginPage?registrationSuccess";
	}

	
	private User createNewExpertAccount(ExpertDTO dtoExpert, BindingResult bindingResult) {
		User registeredUser = null;
		try {
			registeredUser = userService.saveExpert(dtoExpert, "ROLE_EXPERT");
		} catch (UserAlreadyExistsException e) {
			bindingResult.rejectValue(e.isEmailException() ? "email" : "userName", e.isEmailException() ? "message.emailExists" : "message.userNameExists");
			return null;
		}
		return registeredUser;
	}

	@PostMapping("/processRegistrationForm")
	public String proccessRegistrationForm(@Valid @ModelAttribute("user") UserDTO dtoUser, BindingResult bindingResult,
			Model model, HttpServletRequest request) {
		
		if(bindingResult.hasErrors()) {
			return "/user/user-registration-form";
		}
		
		String reversedUserName = reverseString(dtoUser.getUserName());
		
		String newPass = dtoUser.getPassword();
		
		if( newPass.contains(dtoUser.getUserName()) || newPass.contains(reversedUserName)) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "/user/user-registration-form";
		}
		
		User registered = createNewUserAccount(dtoUser, bindingResult);
		if(registered == null) {
			return "/user/user-registration-form";
		}
		
		String token = UUID.randomUUID().toString();
		userService.createVerificationToken(registered, token);
		SimpleMailMessage mail = constructAccountCreatedEmail(getAppUrl(request), request.getLocale(), token, registered);
		
		try {
			logger.info("Przed odpaleniem listenera");
			eventPublisher.publishEvent(new GenericMailEvent(this, mail));
			logger.info("Po odpaleniu listenera");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("registrationError", "Niepowodzenie wysyłania linku aktywacyjnego");
			
			userService.delete(registered);
			
			return "/user/user-registration-form";
		}

		logger.info("Teraz redirect");
		
		return "redirect:/showLoginPage?registrationSuccess";
	}
	
	private User createNewUserAccount(UserDTO dtoUser, BindingResult result) {
		User registeredUser = null;
		try {
			registeredUser = userService.save(dtoUser, "ROLE_USER");
		} catch (UserAlreadyExistsException e) {
			result.rejectValue(e.isEmailException() ? "email" : "userName", e.isEmailException() ? "message.emailExists" : "message.userNameExists");
			return null;
		}
		return registeredUser;
	}
	
	
	@GetMapping("/registrationConfirm")
	public String confirmRegistration(@RequestParam("token") String token, Model model, WebRequest request) {
		
		Locale locale = request.getLocale();
		
		VerificationToken verificationToken = userService.getVerificationToken(token);
		
		if(verificationToken == null) {
			String message = messages.getMessage("auth.message.invalidToken", null, locale);
			model.addAttribute("message", message);
			return "badUser";
		}
		
		  
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpirationDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String messageValue = messages.getMessage("auth.message.expired", null, locale);
	        model.addAttribute("message", messageValue);
	        model.addAttribute("expired", true);
	        model.addAttribute("token", token);
	        return "badUser";
	    } 
	     
	    if(user.isEnabled()) {
	    	return "redirect:/showLoginPage?showAccountWasActive";
	    } else {
	    	user.setEnabled(true); 
		    userService.saveOrUpdateRegisteredUser(user);
			
		    return "redirect:/showLoginPage?showActivateMsg";
	    }
	    
	}
	
	
	@GetMapping("/resendRegistrationToken")
	@ResponseBody
	public GenericResponse resendRegistrationToken(@RequestParam("token") String existingToken, HttpServletRequest request) {
		
		if(existingToken == null || existingToken == "") {
			return new GenericResponse("NoTokenFound", "NoTokenFound");
		}
		
		VerificationToken oldToken = userService.getVerificationToken(existingToken);
		if(oldToken == null) {
			return new GenericResponse("NoTokenFound", "NoTokenFound");
		}
		
		VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
		
		User user = userService.getUser(newToken.getToken());
		String appUrl = "http://" + request.getServerName() + 
			      ":" + request.getServerPort() + 
			      request.getContextPath();
		
		Locale locale = request.getLocale();
		SimpleMailMessage mail = constructResendVerificationTokenEmail(appUrl, locale, newToken, user);
		
		try {
			eventPublisher.publishEvent(new GenericMailEvent(this, mail));
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("Mail Error", "Mail Error");
		}
		
		return new GenericResponse(messages.getMessage("message.resendToken", null, locale));
		
	}
	
	private SimpleMailMessage constructResendVerificationTokenEmail(String appUrl, Locale locale, VerificationToken verificationToken, User user) {
		String url = appUrl + "/register/registrationConfirm?token=" + verificationToken.getToken();
		String message = messages.getMessage("message.resendTokenMailText", null, locale);
		String subject = "QuickFix - Ponowne wysłanie linku aktywującego.";
		
		return constructEmailMessage(subject, message + url, user, locale);
	}
	
	 private SimpleMailMessage constructAccountCreatedEmail(String appUrl, Locale locale, String token, User user) {
		String url = appUrl + "/register/registrationConfirm?token=" + token;
		String message = messages.getMessage("message.confirmMailText", null, locale);
		String subject = "QuickFix - Potwierdzenie Rejestracji";
		return constructEmailMessage(subject, message + url, user, locale);
	}

	private SimpleMailMessage constructEmailMessage(String subject, String body, User user, Locale locale) {
		SimpleMailMessage email = new SimpleMailMessage();
        String signature = messages.getMessage("message.confirmMailSignature", null, locale);
	    email.setSubject(subject);
	    email.setText(body + signature);
	    email.setTo(user.getEmail());
	    email.setFrom(env.getProperty("spring.mail.username"));
	    return email;
	}

	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
	
	private String reverseString(String string) {
		List<Character> charList = new ArrayList<>();
		for(char c : string.toCharArray()) {
			charList.add(c);
		}
		
		Collections.reverse(charList);
		String reversedString = "";
		for(char c : charList) {
			reversedString += c;
		}
		return reversedString;
	}
	
}
