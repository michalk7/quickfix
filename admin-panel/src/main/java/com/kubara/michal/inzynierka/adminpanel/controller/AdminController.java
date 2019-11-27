package com.kubara.michal.inzynierka.adminpanel.controller;

import java.util.List;

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

import com.kubara.michal.inzynierka.adminpanel.dto.AdminDTO;
import com.kubara.michal.inzynierka.adminpanel.entity.Admin;
import com.kubara.michal.inzynierka.adminpanel.exception.AdminAlreadyExistsException;
import com.kubara.michal.inzynierka.adminpanel.service.AdminService;
import com.kubara.michal.inzynierka.adminpanel.utils.StringUtils;

@Controller
@RequestMapping("/admins")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
	
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping({ "", "/" })
	public String getAdminList(Model model) {
		
		List<Admin> adminList = adminService.findAll();
		
		model.addAttribute("admins", adminList);
		
		return "/admin/adminList";
	}
	
	@GetMapping("/add")
	public String getAddPage(Model model) {
		
		model.addAttribute("admin", new AdminDTO());
		model.addAttribute("validated", false);
		
		return "/admin/addAdmin";
	}
	
	@PostMapping("/add")
	public String addNewAdmin(@Valid @ModelAttribute("admin") AdminDTO adminDto, BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "/admin/addAdmin";
		}
		
		String reversedUserName = StringUtils.reverseString(adminDto.getUserName());
		
		String newPass = adminDto.getPassword();
		
		if( newPass.contains(adminDto.getUserName()) || newPass.contains(reversedUserName)) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "/admin/addAdmin";
		}
		
		Admin newAdmin = createNewAdminAccount(adminDto, bindingResult);
		if(newAdmin == null) {
			return "/admin/addAdmin";
		}
		
		return "redirect:/admins?addSuccess";
		
	}

	private Admin createNewAdminAccount(AdminDTO adminDto, BindingResult bindingResult) {
		Admin newAdmin = null;
		try {
			newAdmin = adminService.saveAdmin(adminDto);
		} catch(AdminAlreadyExistsException e) {
			bindingResult.rejectValue(e.isEmailException() ? "email" : "userName", e.isEmailException() ? "message.emailExists" : "message.userNameExists");
			return null;
		}
		return newAdmin;
	}
	
	
}
