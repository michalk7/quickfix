package com.kubara.michal.inzynierka.webapp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kubara.michal.inzynierka.core.entity.Category;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.webapp.service.CategoryService;
import com.kubara.michal.inzynierka.webapp.service.ExpertService;
import com.kubara.michal.inzynierka.webapp.service.UserService;

@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private ExpertService expertService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute("allCategoriesMultiCheckbox")
	public List<Category> getAllCategoriesMultiCheckboxValues() {
		return categoryService.findAll();
	}
	
	@GetMapping( { "", "/" } )
	public String getSearchPage(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, 
			@RequestParam("categoriesGroup") Optional<Integer> categoriesGroup, @RequestParam("onlyMyEstate") Optional<Integer> onlyMyEstate,
			@RequestParam("showAll") Optional<Integer> showAll, Authentication authentication) {
		final int currentPage = page.orElse(1);
		final int pageSize = size.orElse(5);
		
		boolean categoryBooleanValue = categoriesGroup.isPresent() && categoriesGroup.orElse(-1) != -1;
		
		//konwersja obecności parametrów na styl uprawnień (rwx) do plików w linuxie
		//tzn. category = 4, onlyEstate = 2, showAll = 1
		byte one = (byte) (categoryBooleanValue ? 1 : 0);
		byte two = (byte) (onlyMyEstate.isPresent() ? 1 : 0);
		byte three = (byte) (showAll.isPresent() ? 1 : 0);
		
		
		
		User currentUser = userService.findByUserName(authentication.getName());
		String userCity = currentUser.getAddress().getCity();
		
		Estate userEstate = currentUser.getUserEstate();
		
		if(userEstate == null) {
			two = 0;
		}
		
		int resultNumber = (one * 4) + (two * 2) + three;
		
		Category selectedCategory = null;
		if(categoriesGroup.isPresent() && categoriesGroup.orElse(-1) != -1) {
			Optional<Category> categoryOpt = categoryService.findById(categoriesGroup.get());
			selectedCategory = categoryOpt.orElse(null);
		}
		
		Page<User> expertPage = null;
		
		PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);
		
		switch(resultNumber) {
		case 7: {
			//all
			expertPage = expertService.findAllByCategoryAndEstate(selectedCategory, userEstate, pageRequest);
			break;
		}
		case 6: {
			//kategorie i tylko moje osiedle
			expertPage = expertService.findAllByCategoryAndEstateAndCity(selectedCategory, userEstate, userCity, pageRequest);
			break;
		}
		case 5: {
			//kategorie i show all
			expertPage = expertService.findAllByCategory(selectedCategory, pageRequest);
			break;
		}
		case 4: {
			//kategorie
			expertPage = expertService.findAllByCategoryAndCity(selectedCategory, userCity, pageRequest);
			break;
		}
		case 3: {
			//moje osiedle i show all
			expertPage = expertService.findAllByEstate(userEstate, pageRequest);
			break;
		}
		case 2: {
			//moje osiedle
			expertPage = expertService.findAllEstateAndCity(userEstate, userCity, pageRequest);
			break;
		}
		case 1: {
			//show all
			expertPage = expertService.findAll(pageRequest);
			break;
		}
		default: {
			expertPage = expertService.findAllByCity(userCity, pageRequest);
			break;
		}
			
		}
			
		model.addAttribute("userEstate", userEstate);
		
		model.addAttribute("expertPage", expertPage);
		
		int totalPages = expertPage.getTotalPages();
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
		
		return "/search/search";
	}
	
}
