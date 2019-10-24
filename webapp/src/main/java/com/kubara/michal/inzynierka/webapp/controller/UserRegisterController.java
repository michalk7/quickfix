package com.kubara.michal.inzynierka.webapp.controller;

import java.util.Calendar;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
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
import org.springframework.web.context.request.WebRequest;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.core.entity.VerificationToken;
import com.kubara.michal.inzynierka.webapp.dto.UserDTO;
import com.kubara.michal.inzynierka.webapp.registration.OnRegistrationCompleteEvent;
import com.kubara.michal.inzynierka.webapp.service.UserService;
import com.kubara.michal.inzynierka.webapp.validation.UserAlreadyExistsException;

@Controller
@RequestMapping("/register")
public class UserRegisterController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private MessageSource messages;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
	
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/showRegisterPage")
	public String showUserRegisterPage(Model model) {
		model.addAttribute("user", new UserDTO());
		
		return "/user/user-registration-form";
	}
	
	@PostMapping("/processRegistrationForm")
	public String proccessRegistrationForm(@Valid @ModelAttribute("user") UserDTO dtoUser,
			BindingResult bindingResult, Model model, WebRequest request) {
		
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
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("registrationError", "Niepowodzenie wysyłania linku aktywacyjnego");
			return "/user/user-registration-form";
		}
		System.out.println("poszło");
		return "home";
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
			String message = messages.getMessage("auth.message,invalidToken", null, locale);
			model.addAttribute("message", message);
			return "redirect:/badUser";
		}
		
		  
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpirationDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String messageValue = messages.getMessage("auth.message.expired", null, locale);
	        model.addAttribute("message", messageValue);
	        return "redirect:/badUser";
	    } 
	     
	    user.setEnabled(true); 
	    userService.saveOrUpdateRegisteredUser(user);
		
	    return "redirect:/showLoginPage";
	    
	}
	
}
