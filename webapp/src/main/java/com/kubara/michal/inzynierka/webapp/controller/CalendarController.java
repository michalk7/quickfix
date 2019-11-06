package com.kubara.michal.inzynierka.webapp.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

	@GetMapping( { "/", "" } )
	public String getCalendarSite(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> date, Model model) {
		System.out.println("data " + date.orElse(LocalDate.now()).toString());
		return "/calendar/calendar";
	}
	
}
