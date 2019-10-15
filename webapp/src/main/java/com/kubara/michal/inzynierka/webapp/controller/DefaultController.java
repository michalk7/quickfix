package com.kubara.michal.inzynierka.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

	@GetMapping("/")
	public String mainPage() {
		return "home";
	}
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
}
