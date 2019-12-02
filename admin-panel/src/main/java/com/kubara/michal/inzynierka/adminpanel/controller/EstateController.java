package com.kubara.michal.inzynierka.adminpanel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kubara.michal.inzynierka.adminpanel.service.EstateService;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.Street;
import com.kubara.michal.inzynierka.core.entity.User;

@Controller
@RequestMapping("/estates")
public class EstateController {
	
	@Autowired
	private EstateService estateService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
	
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping({ "", "/" })
	public String getEstatesList(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		
		final int currentPage = page.orElse(1);
		final int pageSize = size.orElse(5);
		
		PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);
		
		Page<Estate> estatePage = estateService.findAll(pageRequest);
		
		model.addAttribute("estatePage", estatePage);
		
		int totalPages = estatePage.getTotalPages();
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
		
		return "/estate/estateList";
		
	}
	
	@GetMapping("details/{estateId}")
	public String getEstateDetails(@PathVariable("estateId") long estateId, Model model) {
		Optional<Estate> estateOpt = estateService.findById(estateId);
	
		if(!estateOpt.isPresent()) {
			return null;
		}
		
		Estate estate = estateOpt.get();
		
		List<Street> streets = new ArrayList<Street>(estate.getStreets());
		List<User> experts = new ArrayList<User>(estate.getExperts());
		List<User> users = new ArrayList<User>(estate.getUsers());
		
		model.addAttribute("estateName", estate.getName());
		model.addAttribute("streets", streets);
		model.addAttribute("experts", experts);
		model.addAttribute("users", users);
		
		return "/estate/estateDetails";
	}
	
}
