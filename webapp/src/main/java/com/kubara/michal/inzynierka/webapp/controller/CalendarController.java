package com.kubara.michal.inzynierka.webapp.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kubara.michal.inzynierka.core.entity.Event;
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
	public String getCalendarSite(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> dateOpt, 
			Model model, Authentication authentication) {
		
		LocalDate date = dateOpt.orElse(LocalDate.now());
		
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
		
		List<Event> events = null;
		
		LocalDateTime startDate = LocalDateTime.of(date, LocalTime.of(0, 0));
		LocalDateTime endDate = LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0));
		
		if(user.getRoles().stream().anyMatch(e -> e.getName().equals("ROLE_EXPERT"))) {
			events = calendarService.findAllByDateBetweenAndExpert(startDate, endDate, user);
			model.addAttribute("userType", "expert");
		} else if(user.getRoles().stream().anyMatch(e -> e.getName().equals("ROLE_USER"))) {
			events = calendarService.findAllByDateBetweenAndUser(startDate, endDate, user);
			model.addAttribute("userType", "user");
		} else {
			events = new ArrayList<>();
			model.addAttribute("userType", "undefinedUserType");
		}
		
		model.addAttribute("calendarDate", date);
		model.addAttribute("eventsList", events);
		
		
		return "/calendar/calendar";
	}
	
	
}
