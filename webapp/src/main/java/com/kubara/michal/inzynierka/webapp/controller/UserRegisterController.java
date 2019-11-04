package com.kubara.michal.inzynierka.webapp.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

import com.kubara.michal.inzynierka.core.entity.Category;
import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.core.entity.VerificationToken;
import com.kubara.michal.inzynierka.webapp.dto.ExpertDTO;
import com.kubara.michal.inzynierka.webapp.dto.UserDTO;
import com.kubara.michal.inzynierka.webapp.registration.OnRegistrationCompleteEvent;
import com.kubara.michal.inzynierka.webapp.registration.OnTokenResendEvent;
import com.kubara.michal.inzynierka.webapp.service.CategoryService;
import com.kubara.michal.inzynierka.webapp.service.UserService;
import com.kubara.michal.inzynierka.webapp.util.GenericResponse;
import com.kubara.michal.inzynierka.webapp.validation.UserAlreadyExistsException;

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
	Environment env;
	
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
		//return categoryService.findAll().stream().map(e -> e.getName()).collect(Collectors.toList());
		return categoryService.findAll();
	}
	
	
	@PostMapping("/processExpertRegistrationForm")
	public String proccessExpertRegistrationForm(@Valid @ModelAttribute("expert") ExpertDTO dtoExpert, 
			BindingResult bindingResult, Model model, WebRequest request) {
		
		if(bindingResult.hasErrors()) {
			return "/expert/expert-registration-form";
		}
		
		if(dtoExpert.getSelectedCategoriesFromCheckboxes().size() > 4 || dtoExpert.getSelectedCategoriesFromCheckboxes().size() < 1) {
			return "/expert/expert-registration-form";
		}
		

		User registered = createNewExpertAccount(dtoExpert, bindingResult);
		if(registered == null) {
			return "/expert/expert-registration-form";
		}
		
		try {
			String appUrl = request.getContextPath();
			logger.info("Przed odpaleniem listenera");
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
			logger.info("Po odpaleniu listenera");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("registrationError", "Niepowodzenie wysyłania linku aktywacyjnego");
			
			userService.delete(registered);
			
			return "/expert/expert-registration-form";
		}
		//System.out.println("poszło");
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
			Model model, WebRequest request) {
		
		//validated = true;
		
		if(bindingResult.hasErrors()) {
			return "/user/user-registration-form";
		}
		
//		User existingUser = userService.findByUserName(dtoUser.getUserName());
		
//		if(existingUser != null) {
//			model.addAttribute("user", new UserDTO());
//			model.addAttribute("registrationError", "Podana nazwa użytkownika jest już zajęta");
//			
//			return "/user/user-registration-form";
//		}
		
		User registered = createNewUserAccount(dtoUser, bindingResult);
		if(registered == null) {
			return "/user/user-registration-form";
		}
		
		try {
			String appUrl = request.getContextPath();
			logger.info("Przed odpaleniem listenera");
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
			logger.info("Po odpaleniu listenera");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("registrationError", "Niepowodzenie wysyłania linku aktywacyjnego");
			
			userService.delete(registered);
			
			return "/user/user-registration-form";
		}
		//System.out.println("poszło");
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
		
		VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
		
		User user = userService.getUser(newToken.getToken());
		String appUrl = "http://" + request.getServerName() + 
			      ":" + request.getServerPort() + 
			      request.getContextPath();
		
		Locale locale = request.getLocale();
		SimpleMailMessage mail = constructResendVerificationTokenEmail(appUrl, locale, newToken, user);
		
		eventPublisher.publishEvent(new OnTokenResendEvent(user, request.getLocale(), appUrl, newToken, mail));
		
		return new GenericResponse(messages.getMessage("message.resendToken", null, locale));
		
	}
	
	private SimpleMailMessage constructResendVerificationTokenEmail(String appUrl, Locale locale, VerificationToken verificationToken, User user) {
		String url = appUrl + "/register/registrationConfirm?token=" + verificationToken.getToken();
		String message = messages.getMessage("message.resendTokenMailText", null, locale);
        String signature = messages.getMessage("message.confirmMailSignature", null, locale);
        String recipientAddress = user.getEmail();
		String subject = "QuickFix - Ponowne wysłanie linku aktywującego.";
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + url + signature);
        email.setFrom(env.getProperty("spring.mail.username"));
        return email;
	}
	
	
	
}
