package com.kubara.michal.quickfix.adminpanel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

	@GetMapping("/")
	public String mainPage() {
		return "home";
	}
	
}
