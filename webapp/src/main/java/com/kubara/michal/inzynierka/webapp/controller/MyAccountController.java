package com.kubara.michal.inzynierka.webapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.webapp.dto.PasswordChangeDTO;
import com.kubara.michal.inzynierka.webapp.dto.UserEditDTO;
import com.kubara.michal.inzynierka.webapp.service.UserService;

@Controller
@RequestMapping("/myAccount")
public class MyAccountController {

	@Autowired
	private UserService userService;
	
	@GetMapping({ "/", "" })
	public String getMyAccountPage(Model model, Authentication authentication) {
		
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
		
		model.addAttribute("validated", false);
		model.addAttribute("passValidated", false);
		model.addAttribute("user", getUserEditDTOFromUser(user));
		model.addAttribute("passwordChange", new PasswordChangeDTO());
		model.addAttribute("dataTab", true);
		
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
		
		
		//tą linie zmienić, bo zależy od walidacji itp
		model.addAttribute("user", getUserEditDTOFromUser(user));
		
		
		
		//tu redirect z success message raczej
		return "/myAccount/myAccount";
		
	}
	
	@PutMapping("/changePassword")
	public String handleChangePassword(@Valid @ModelAttribute("password") PasswordChangeDTO passwordChange, BindingResult bindingResult, Model model, Authentication authentication ) {
		
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
		
		model.addAttribute("changePasswordTab", true);
		model.addAttribute("validated", false);
		model.addAttribute("user", getUserEditDTOFromUser(user));
		
		if(bindingResult.hasErrors()) {
			return "/myAccount/myAccount";
		}
		
		
		
		//tą linie zmienić, bo zależy od walidacji itp
		model.addAttribute("passwordChange", new PasswordChangeDTO());
		
		//tu redirect z success message raczej
		return "/myAccount/myAccount";
		
	}
	
	private UserEditDTO getUserEditDTOFromUser(User user) {
		UserEditDTO userEdit = new UserEditDTO();
		
		userEdit.setUserName(user.getUserName());
		userEdit.setFirstName(user.getFirstName());
		userEdit.setLastName(user.getLastName());
		userEdit.setEmail(user.getEmail());
		userEdit.setCity(user.getAddress().getCity());
		userEdit.setPostCity(user.getAddress().getPostCity());
		userEdit.setPostCode(user.getAddress().getPostCode());
		userEdit.setStreet(user.getAddress().getStreet());
		userEdit.setHouseNumber(user.getAddress().getHouseNumber());
		userEdit.setApartmentNumber(user.getAddress().getApartmentNumber());
		userEdit.setPhoneNumber(user.getAddress().getPhoneNumber());
		
		return userEdit;
	}
	
}
