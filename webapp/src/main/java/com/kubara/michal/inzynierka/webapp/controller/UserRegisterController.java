package com.kubara.michal.inzynierka.webapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubara.michal.inzynierka.webapp.dto.UserDTO;
import com.kubara.michal.inzynierka.webapp.entity.User;
import com.kubara.michal.inzynierka.webapp.service.UserService;

@Controller
@RequestMapping("/register")
public class UserRegisterController {

	@Autowired
	private UserService userService;
	
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
			BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "/user/user-registration-form";
		}
		
		User existingUser = userService.findByUserName(dtoUser.getUserName());
		
		if(existingUser != null) {
			model.addAttribute("user", new UserDTO());
			model.addAttribute("registrationError", "Podana nazwa użytkownika jest już zajęta");
			
			return "/user/user-registration-form";
		}
		
		userService.save(dtoUser, "ROLE_USER");
		
		return "home";
	}
	
	
	
}
