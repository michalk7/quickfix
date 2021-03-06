package com.kubara.michal.quickfix.webapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kubara.michal.quickfix.core.entity.User;
import com.kubara.michal.quickfix.webapp.dto.SetPasswordDTO;
import com.kubara.michal.quickfix.webapp.event.GenericMailEvent;
import com.kubara.michal.quickfix.webapp.service.UserService;
import com.kubara.michal.quickfix.webapp.service.UserValidationService;
import com.kubara.michal.quickfix.webapp.util.GenericResponse;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private UserValidationService userValidationService;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/showResetPasswordPage")
	public String getResetPasswordPage() {
		return "forgotPassword";
	}
	
	@PostMapping("/resetPassword")
	@ResponseBody
	public GenericResponse resetPassword(@RequestParam("email") String email, HttpServletRequest request) {
		User user = userService.findByEmail(email);
		if(user == null) {
			return new GenericResponse("Nie znaleziono u??ytkownika o podanym adresie email.", "UserNotFound");
		}
		
		String token = UUID.randomUUID().toString();
		
		userService.createResetPasswordToken(user, token);
		
		SimpleMailMessage mail = constructResetPasswordTokenEmail(getAppUrl(request), request.getLocale(), token, user);
		
		try {
			eventPublisher.publishEvent(new GenericMailEvent(this, mail));
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("Wys??anie maila nie powiod??o si??.", "Mail Error");
		}
		
		return new GenericResponse(messages.getMessage("message.resetPasswordToken", null, request.getLocale()));
		
	}
	
	@GetMapping("/changePassword")
	public String showSetNewPasswordPage(@RequestParam("id") long id, @RequestParam("token") String token, Model model, HttpServletRequest request) {
		String result = userValidationService.validateResetPasswordToken(id, token);
		if(result != null) {
			model.addAttribute("errorPasswordChangeMsg", messages.getMessage("auth.message." + result, null, request.getLocale()));
			return "redirect:/showLoginPage";
		}
		return "redirect:/user/setNewPassword";
	}
	
	@GetMapping("/setNewPassword")
	public String showSetNewPasswordPage(Model model) {
		model.addAttribute("passwordChange", new SetPasswordDTO());
		model.addAttribute("passValidated", true);
		return "setNewPassword";
	}
	
	@PostMapping("savePassword")
	public String savePassword(@Valid @ModelAttribute("passwordChange") SetPasswordDTO passwordDTO, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "setNewPassword";
		}

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<Character> usernameRev = new ArrayList<>();
		for(char c : user.getUserName().toCharArray()) {
			usernameRev.add(c);
		}
		
		Collections.reverse(usernameRev);
		String reversedUserName = "";
		for(char c : usernameRev) {
			reversedUserName += c;
		}
		
		String newPass = passwordDTO.getPassword();
		
		if( newPass.contains(user.getUserName()) || newPass.contains(reversedUserName)) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "setNewPassword";
		}
		
		userService.setNewPassword(user, passwordDTO.getPassword());
		
		userService.deleteResetToken(user.getResetPasswordToken());
		
		return "redirect:/showLoginPage?setPasswordSuccess";
	}
	

    private SimpleMailMessage constructResetPasswordTokenEmail(String appUrl, Locale locale, String token, User user) {
		String url = appUrl + "/user/changePassword?id=" + user.getId() + "&token=" + token;
		String message = messages.getMessage("message.resetPasswordMailText", null, locale);
		return constructEmailMessage("QuickFix - Odzyskiwanie has??a", message + url, user, locale);
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
    
}
