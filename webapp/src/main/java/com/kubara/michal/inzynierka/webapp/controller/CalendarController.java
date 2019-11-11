package com.kubara.michal.inzynierka.webapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.webapp.service.CalendarService;
import com.kubara.michal.inzynierka.webapp.service.UserService;

@Controller
@RequestMapping("/calendar")
public class CalendarController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CalendarService calendarService;

	@GetMapping( { "/", "" } )
	public String getCalendarSite(Model model, Authentication authentication) {
		
		
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
		
		
		
		if(user.getRoles().stream().anyMatch(e -> e.getName().equals("ROLE_EXPERT"))) {
			model.addAttribute("userType", "expert");
			
			long notConfirmedCount = calendarService.findAllByExpert(user).stream().filter(e -> !e.isConfirmed()).count();
			
			model.addAttribute("notConfirmedCount", notConfirmedCount);
			
		} else if(user.getRoles().stream().anyMatch(e -> e.getName().equals("ROLE_USER"))) {
			model.addAttribute("userType", "user");
		} else {
			model.addAttribute("userType", "undefinedUserType");
		}
		
		return "/calendar/calendar";
	}
	
	
}
