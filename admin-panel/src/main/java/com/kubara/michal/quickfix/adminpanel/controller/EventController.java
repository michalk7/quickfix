package com.kubara.michal.quickfix.adminpanel.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kubara.michal.quickfix.adminpanel.service.EventService;
import com.kubara.michal.quickfix.core.entity.Event;

@Controller
@RequestMapping("/events")
public class EventController {
	
	@Autowired
	private EventService eventService;

	@GetMapping({ "", "/" })
	public String getEventList(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, Model model) {
		final int currentPage = page.orElse(1);
		final int pageSize = size.orElse(5);
		
		PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);
		
		Page<Event> eventPage = eventService.findAll(pageRequest);
		
		model.addAttribute("eventPage", eventPage);
		
		int totalPages = eventPage.getTotalPages();
		if(totalPages > 0) {
			int lastPage = currentPage + 9;
			
			if(lastPage > totalPages) {
				lastPage = totalPages;
			}
			
			int tmpCurrentPage = currentPage;
			if( (lastPage - tmpCurrentPage + 1) < 10) {
				if(lastPage <= 10) {
					tmpCurrentPage = 1;
				} else {
					tmpCurrentPage = lastPage - 9;
				}
			}
			
			List<Integer> pageNumbers = IntStream.rangeClosed(tmpCurrentPage, lastPage)
	                .boxed()
	                .collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		
		return "/event/eventList";
		
	}
	
}
