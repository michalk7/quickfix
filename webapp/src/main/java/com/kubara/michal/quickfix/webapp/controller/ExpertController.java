package com.kubara.michal.quickfix.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubara.michal.quickfix.core.entity.Category;
import com.kubara.michal.quickfix.webapp.dto.ExpertDTO;
import com.kubara.michal.quickfix.webapp.service.CategoryService;

@Controller
@RequestMapping("/expert")
public class ExpertController {
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute("allCategoriesMultiCheckbox")
	public List<Category> getAllCategoriesMultiCheckboxValues() {
		return categoryService.findAll();
	}

	@GetMapping( { "", "/" } )
	public String getExpertHome() {
		return "/expert/expert-home";
	}
	
	@GetMapping("/showRegisterPage")
	public String getExpertRegisterPage(Model model) {
		model.addAttribute("expert", new ExpertDTO());
		model.addAttribute("validated", false);
		
		return "/expert/expert-registration-form";
	}
	
}
