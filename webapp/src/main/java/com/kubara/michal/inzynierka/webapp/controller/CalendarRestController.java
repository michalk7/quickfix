package com.kubara.michal.inzynierka.webapp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kubara.michal.inzynierka.core.entity.Event;
import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.webapp.dto.EventDTO;
import com.kubara.michal.inzynierka.webapp.dto.EventSaveDTO;
import com.kubara.michal.inzynierka.webapp.service.CalendarService;
import com.kubara.michal.inzynierka.webapp.service.UserService;
import com.kubara.michal.inzynierka.webapp.util.GenericResponse;
import com.kubara.michal.inzynierka.webapp.util.NotConfirmedCountResponse;

@RestController
@RequestMapping("/calendar/api")
public class CalendarRestController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CalendarService calendarService;
	
	@GetMapping("/getList")
	public List<EventDTO> getUserEventList(@RequestParam(value = "start", required = false) Optional<String> start, 
			@RequestParam(value = "end", required = false) Optional<String> end, Authentication authentication) {
		
		LocalDateTime dateStart = null;
		LocalDateTime dateEnd = null;
		boolean dateRange = false;
		
		if(start.isPresent() && end.isPresent()) {
			
			dateStart = LocalDateTime.parse(start.get(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			dateEnd = LocalDateTime.parse(end.get(),  DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			
			dateRange = true;
			
		}
		
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
		
		List<Event> events = null;
		
		if(user.getRoles().stream().anyMatch(e -> e.getName().equals("ROLE_EXPERT"))) {
			events = dateRange ? calendarService.findAllByDateBetweenAndExpert(dateStart, dateEnd, user) : calendarService.findAllByExpert(user);
		} else if(user.getRoles().stream().anyMatch(e -> e.getName().equals("ROLE_USER"))) {
			events = dateRange ? calendarService.findAllByDateBetweenAndUser(dateStart, dateEnd, user) : calendarService.findAllByUser(user);
		} else {
			events = new ArrayList<>();
		}
		
		List<EventDTO> dtoEvents = events.stream().map(e -> {
			String color = e.isConfirmed() ? "#2a67a3" : "#d3d3d3";
			String textColor = e.isConfirmed() ? "white" : "black";
			
			EventDTO dtoEvent = new EventDTO(e.getId(), e.getEventName(), e.getStartDate().toString(), 
									e.getEndDate().toString(), e.getProblemTitle(), e.getProblemDescription(), color, textColor);
			return dtoEvent;
		}).collect(Collectors.toList());
		
		return dtoEvents;
		
	}
	
	@GetMapping("/getNotConfirmedCount")
	public NotConfirmedCountResponse getNotConfirmedCount(Authentication authentication) {
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
		
		if(user.getRoles().stream().anyMatch(e -> e.getName().equals("ROLE_EXPERT"))) {
			long notConfirmedCount = calendarService.findAllByExpert(user).stream().filter(e -> !e.isConfirmed()).count();
			
			return new NotConfirmedCountResponse(notConfirmedCount);
		}
		
		return new NotConfirmedCountResponse(0, "User hasn't have a expert role");
		
	}
	
	@GetMapping("/getExpertCalendar/{expertId}")
	public List<EventDTO> getExpertEventList(@RequestParam(value = "start", required = false) Optional<String> start, 
			@RequestParam(value = "end", required = false) Optional<String> end, @PathVariable("expertId") long expertId, Authentication authentication)  {
		LocalDateTime dateStart = null;
		LocalDateTime dateEnd = null;
		boolean dateRange = false;
		
		if(start.isPresent() && end.isPresent()) {
			
			dateStart = LocalDateTime.parse(start.get(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			dateEnd = LocalDateTime.parse(end.get(),  DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			
			dateRange = true;
			
		}
		
		Optional<User> userOpt = userService.findById(expertId);
		
		if(!userOpt.isPresent()) {
			return new ArrayList<EventDTO>();
		}
		
		User user = userOpt.orElse(new User());
		
		List<Event> events = null;
		
		if(user.getRoles().stream().anyMatch(e -> e.getName().equals("ROLE_EXPERT"))) {
			events = dateRange ? calendarService.findAllByDateBetweenAndExpert(dateStart, dateEnd, user) : calendarService.findAllByExpert(user);
		} else {
			events = new ArrayList<>();
		}
		
		List<EventDTO> dtoEvents = events.stream().map(e -> {
			String color = e.isConfirmed() ? "#2a67a3" : "#d3d3d3";
			String textColor = e.isConfirmed() ? "white" : "black";
			String titleText = e.isConfirmed() ? "Potwierdzone" : "Niepotwierdzone";
			
			if(e.getUser().getUserName().equals(authentication.getName())) {
				color = "#ede007";
				textColor = "black";
			}
			
			EventDTO dtoEvent = new EventDTO(e.getId(), titleText, e.getStartDate().toString(), 
									e.getEndDate().toString(), "", "", color, textColor);
			return dtoEvent;
		}).collect(Collectors.toList());
		
		return dtoEvents;
		
	}
	
	@PostMapping("/saveEvent/{expertId}")
	public GenericResponse saveEvent(@PathVariable("expertId") long expertId, @RequestBody EventSaveDTO eventToSave, Authentication authentication ) {
		
		Optional<User> expertOpt = userService.findById(expertId);
		
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
		
		if(!expertOpt.isPresent()) {
			return new GenericResponse("Wrong expert id", "Wrong expert id");
		}
		
		LocalDateTime dateStart = LocalDateTime.parse(eventToSave.getStartDate(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		LocalDateTime dateEnd = LocalDateTime.parse(eventToSave.getEndDate(),  DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		
		User expert = expertOpt.orElse(new User());
		
		Event event = new Event(dateStart, dateEnd, "Usługa u " + user.getUserName() + " realizowana przez " + expert.getUserName(), 
				eventToSave.getProblemTitle(), eventToSave.getProblemDescription(), false);
		
		event.setExpert(expert);
		event.setUser(user);
		
		event.setId(0);
		
		Event savedEvent = calendarService.save(event);
		
		if(savedEvent == null) {
			return new GenericResponse("Save Error", "Save Error");
		}
		
		return new GenericResponse("Saved");
		
	}
	
}
