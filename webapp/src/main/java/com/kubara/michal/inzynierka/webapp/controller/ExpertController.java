package com.kubara.michal.inzynierka.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubara.michal.inzynierka.webapp.dto.UserDTO;

@Controller
@RequestMapping("/expert")
public class ExpertController {

	@GetMapping( { "", "/" } )
	public String getExpertHome() {
		return "/expert/expert-home";
	}
	
	@GetMapping("/showRegisterPage")
	public String getExpertRegisterPage(Model model) {
		model.addAttribute("expert", new UserDTO());
		
		return "/expert/expert-registration-form";
	}
	
}
