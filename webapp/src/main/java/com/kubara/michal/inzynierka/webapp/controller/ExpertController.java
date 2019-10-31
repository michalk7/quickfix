package com.kubara.michal.inzynierka.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/expert")
public class ExpertController {

	@GetMapping("")
	public String getExpertHome() {
		return "/expert/expert-home";
	}
	
	
}
