package com.kubara.michal.inzynierka.webapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.webapp.dto.PasswordChangeDTO;
import com.kubara.michal.inzynierka.webapp.dto.UserEditDTO;
import com.kubara.michal.inzynierka.webapp.service.UserService;
import com.kubara.michal.inzynierka.webapp.validation.UserAlreadyExistsException;
import com.kubara.michal.inzynierka.webapp.validation.WrongPasswordException;

@Controller
@RequestMapping("/myAccount")
public class MyAccountController {

	@Autowired
	private UserService userService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
	
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping({ "/", "" })
	public String getMyAccountPage(@RequestParam(value = "passTab", required = false) Optional<Integer> passTab, Model model, Authentication authentication) {
		
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
		
		model.addAttribute("validated", false);
		model.addAttribute("passValidated", false);
		model.addAttribute("user", getUserEditDTOFromUser(user));
		model.addAttribute("passwordChange", new PasswordChangeDTO());
		
		if(passTab.isPresent()) {
			model.addAttribute("changePasswordTab", true);
		} else {
			model.addAttribute("dataTab", true);
		}
		
		
		return "/myAccount/myAccount";
	}
	
	@PutMapping("/editUserData")
	public String handleEditUserData(@Valid @ModelAttribute("user") UserEditDTO userEdit, BindingResult bindingResult, Model model, Authentication authentication) {
		
		model.addAttribute("passValidated", false);
		model.addAttribute("passwordChange", new PasswordChangeDTO());
		model.addAttribute("dataTab", true);
		
		if(bindingResult.hasErrors()) {
			return "/myAccount/myAccount";
		}
		
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
		
		User editedUser = editUserAccount(user, userEdit, bindingResult);
		
		if(editedUser == null) {
			return "/myAccount/myAccount";
		}
		
		//tu redirect z success message raczej
		return "redirect:/myAccount?editSuccess";
		
	}
	
	@PutMapping("/changePassword")
	public String handleChangePassword(@Valid @ModelAttribute("passwordChange") PasswordChangeDTO passwordChange, BindingResult bindingResult, Model model, Authentication authentication ) {
		
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
		
		model.addAttribute("changePasswordTab", true);
		model.addAttribute("validated", false);
		model.addAttribute("user", getUserEditDTOFromUser(user));
		
		if(bindingResult.hasErrors()) {
			return "/myAccount/myAccount";
		}

		List<Character> usernameRev = new ArrayList<>();
		for(char c : user.getUserName().toCharArray()) {
			usernameRev.add(c);
		}
		
		Collections.reverse(usernameRev);
		String reversedUserName = "";
		for(char c : usernameRev) {
			reversedUserName += c;
		}
		
		String newPass = passwordChange.getPassword();
		
		if( newPass.contains(user.getUserName()) || newPass.contains(reversedUserName)) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "/myAccount/myAccount";
		}
		
		try {
			userService.changePassword(user, passwordChange);
		} catch (WrongPasswordException e) {
			bindingResult.rejectValue("oldPassword", "message.wrongPassword");
			return "/myAccount/myAccount";
		}
		
		//tu redirect z success message raczej
		return "redirect:/myAccount?passwordChangeSuccess&passTab=1";
		
	}
	
	private User editUserAccount(User userToEdit, UserEditDTO dtoUser, BindingResult result) {
		User user = null;
		try {
			user = userService.update(userToEdit, dtoUser);
		} catch (UserAlreadyExistsException e) {
			result.rejectValue(e.isEmailException() ? "email" : "userName", e.isEmailException() ? "message.emailExists" : "message.userNameExists");
			return null;
		}
		return user;
	}
	
	private UserEditDTO getUserEditDTOFromUser(User user) {
		UserEditDTO userEdit = new UserEditDTO();
		
		userEdit.setUserName(user.getUserName());
		userEdit.setFirstName(user.getFirstName());
		userEdit.setLastName(user.getLastName());
		userEdit.setCity(user.getAddress().getCity());
		userEdit.setPostCity(user.getAddress().getPostCity());
		userEdit.setPostCode(user.getAddress().getPostCode());
		userEdit.setStreet(user.getAddress().getStreet());
		userEdit.setDistrict(user.getAddress().getDistrict());
		userEdit.setHouseNumber(user.getAddress().getHouseNumber());
		userEdit.setApartmentNumber(user.getAddress().getApartmentNumber());
		userEdit.setPhoneNumber(user.getAddress().getPhoneNumber());
		
		return userEdit;
	}
	
}
