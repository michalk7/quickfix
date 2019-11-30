package com.kubara.michal.inzynierka.adminpanel.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kubara.michal.inzynierka.adminpanel.dto.ExpertDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.ExpertDetailsDTO;
import com.kubara.michal.inzynierka.adminpanel.exception.UserAlreadyExistsException;
import com.kubara.michal.inzynierka.adminpanel.service.CategoryService;
import com.kubara.michal.inzynierka.adminpanel.service.ExpertService;
import com.kubara.michal.inzynierka.adminpanel.utils.GenericResponse;
import com.kubara.michal.inzynierka.adminpanel.utils.StringUtils;
import com.kubara.michal.inzynierka.core.entity.Category;
import com.kubara.michal.inzynierka.core.entity.User;

@Controller
@RequestMapping("/experts")
public class ExpertController {
	
	@Autowired
	private ExpertService expertService;
	
	@Autowired
	private CategoryService categoryService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
	
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@ModelAttribute("allCategoriesMultiCheckbox")
	public List<Category> getAllCategoriesMultiCheckboxValues() {
		return categoryService.findAll();
	}

	@GetMapping({ "", "/" })
	public String getExpertList(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		
		final int currentPage = page.orElse(1);
		final int pageSize = size.orElse(5);
		
		PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);
		
		Page<User> expertPage = expertService.findAll(pageRequest);
		
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
		
		return "/expert/expertList";
		
	}
	
	@GetMapping("/details/{expertId}")
	@ResponseBody
	public ExpertDetailsDTO getExpertDetails(@PathVariable("expertId") long expertId) {
		
		Optional<User> expertOpt = expertService.findById(expertId);
		
		if(!expertOpt.isPresent()) {
			return null;
		}
		
		User expert = expertOpt.get();
		
		List<String> categories = expert.getCategories().stream().map(e -> e.getName()).collect(Collectors.toList());
		
		ExpertDetailsDTO expertToSend = new ExpertDetailsDTO(
				expert.getAddress().getCity(),
				expert.getAddress().getDistrict(),
				expert.getAddress().getPostCode(),
				expert.getAddress().getPostCity(),
				expert.getAddress().getStreet(),
				expert.getAddress().getHouseNumber(), 
				expert.getAddress().getApartmentNumber(),
				expert.getAddress().getPhoneNumber(),
				categories);
		
		return expertToSend;
	}
	
	@DeleteMapping("/delete/{expertId}")
	@ResponseBody
	public GenericResponse deleteExpert(@PathVariable("expertId") long expertId) {
		Optional<User> expertOpt = expertService.findById(expertId);
		
		if(!expertOpt.isPresent()) {
			return new GenericResponse("Brak experta o podanym id", "Wrong ID");
		}
		
		User expert = expertOpt.get();
		
		if(!expertService.isExpert(expert)) {
			return new GenericResponse("Użytkownik nie jest fachowcem", "Wrong User type");
		}
		
		expertService.delete(expert);
		
		return new GenericResponse("Pomyślnie usunięto fachowca");
	}
	
	@GetMapping("/add")
	public String getAddExpertPage(Model model) {
		model.addAttribute("expert", new ExpertDTO());
		model.addAttribute("validated", false);
		
		return "/expert/addExpert";
	}
	
	@PostMapping("/add")
	public String addNewExpert(@Valid @ModelAttribute("expert") ExpertDTO expertDto, 
			BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "/expert/addExpert";
		}
		
		String reversedUserName = StringUtils.reverseString(expertDto.getUserName());
	
		String newPass = expertDto.getPassword();
		
		if(newPass.contains(expertDto.getUserName()) || newPass.contains(reversedUserName)) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "/expert/addExpert";
		}
		
		User newExpert = createNewExpertAccount(expertDto, bindingResult);
		if(newExpert == null) {
			return "/expert/addExpert";
		}
		
		return "redirect:/experts?addSuccess";
	}

	private User createNewExpertAccount(ExpertDTO expertDto, BindingResult bindingResult) {
		User newExpert = null;
		try {
			newExpert = expertService.saveExpert(expertDto);
		} catch(UserAlreadyExistsException e) {
			bindingResult.rejectValue(e.isEmailException() ? "email" : "userName", e.isEmailException() ? "message.emailExists" : "message.userNameExists");
			return null;
		}
		return newExpert;
	}
	
	
}
