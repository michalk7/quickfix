package com.kubara.michal.quickfix.adminpanel.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kubara.michal.quickfix.adminpanel.dto.AccountStatusDTO;
import com.kubara.michal.quickfix.adminpanel.dto.AccountVerificationDTO;
import com.kubara.michal.quickfix.adminpanel.dto.EstateChoiceDTO;
import com.kubara.michal.quickfix.adminpanel.dto.ExpertDTO;
import com.kubara.michal.quickfix.adminpanel.dto.ExpertDetailsDTO;
import com.kubara.michal.quickfix.adminpanel.dto.ExpertEditDTO;
import com.kubara.michal.quickfix.adminpanel.dto.PasswordChangeDTO;
import com.kubara.michal.quickfix.adminpanel.exception.UserAlreadyExistsException;
import com.kubara.michal.quickfix.adminpanel.service.CategoryService;
import com.kubara.michal.quickfix.adminpanel.service.EstateService;
import com.kubara.michal.quickfix.adminpanel.service.ExpertService;
import com.kubara.michal.quickfix.adminpanel.utils.GenericResponse;
import com.kubara.michal.quickfix.adminpanel.utils.StringUtils;
import com.kubara.michal.quickfix.core.entity.Category;
import com.kubara.michal.quickfix.core.entity.Estate;
import com.kubara.michal.quickfix.core.entity.User;

@Controller
@RequestMapping("/experts")
public class ExpertController {
	
	@Autowired
	private ExpertService expertService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private EstateService estateService;
	
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
		List<String> expertEstates = expert.getExpertEstates().stream().map(e -> e.getName()).collect(Collectors.toList());
		
		ExpertDetailsDTO expertToSend = new ExpertDetailsDTO(
				expert.getAddress().getCity(),
				expert.getAddress().getDistrict(),
				expert.getAddress().getPostCode(),
				expert.getAddress().getPostCity(),
				expert.getAddress().getStreet(),
				expert.getAddress().getHouseNumber(), 
				expert.getAddress().getApartmentNumber(),
				expert.getAddress().getPhoneNumber(),
				categories,
				expertEstates);
		
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
	
	@GetMapping("/edit/{expertId}")
	public String getEditPage(@PathVariable("expertId") long expertId, Model model) {
		
		Optional<User> expertOpt = expertService.findById(expertId);
		
		if(!expertOpt.isPresent()) {
			return "redirect:/experts?wrongId";
		}
		
		model.addAttribute("expert", getExpertDtoFromUser(expertOpt.get()));
		model.addAttribute("validated", false);
		
		return "/expert/editExpert";
	}
	
	@PutMapping("/editUserData")
	public String editUserData(@Valid @ModelAttribute("expert") ExpertEditDTO expertDto, BindingResult bindingResult,
			Model model) {
		if(bindingResult.hasErrors()) {
			return "/expert/editExpert";
		}
		
		Optional<User> expertOpt = expertService.findById(expertDto.getId());
		if(!expertOpt.isPresent()) {
			return "/expert/editExpert";
		}
		
		User editedExpert = editExpertAccount(expertOpt.get(), expertDto, bindingResult);
		
		if(editedExpert == null) {
			return "/expert/editExpert";
		}
		
		return "redirect:/experts?editSuccess";
		
	}
	
	@PutMapping("/changeAccountStatus/{expertId}")
	@ResponseBody
	public GenericResponse changeAccountStatus(@PathVariable("expertId") long expertId, @RequestBody AccountStatusDTO accountStatusDTO) {
		 Optional<User> expertOpt = expertService.findById(expertId);
		 
		 if(!expertOpt.isPresent()) {
			 return new GenericResponse("Niepoprawne id fachowca", "Wrong expert id");
		 }
		 
		 User expert = expertOpt.get();
		 
		 expert.setEnabled(accountStatusDTO.isEnabled());
		 User savedExpert = expertService.save(expert);
		 
		 if(savedExpert == null) {
			 return new GenericResponse("Błąd zapisu", "Save Error");
		 }
		 
		 return new GenericResponse("Zmieniono status konta użytkownika");
		 
	}
	
	@PutMapping("/verifyAccount/{expertId}")
	@ResponseBody
	public GenericResponse verifyAccount(@PathVariable("expertId") long expertId, @RequestBody AccountVerificationDTO accountVerificationDTO) {
		Optional<User> expertOpt = expertService.findById(expertId);
		 
		if(!expertOpt.isPresent()) {
			return new GenericResponse("Niepoprawne id fachowca", "Wrong expert id");
		}
		 
		User expert = expertOpt.get();
		 
		expert.setVerified(accountVerificationDTO.isVerified());
		User savedExpert = expertService.save(expert);
		 
		if(savedExpert == null) {
			return new GenericResponse("Błąd zapisu", "Save Error");
		}
		 
		return new GenericResponse("Zmieniono status weryfikacji konta użytkownika");
	}
	
	@GetMapping("/assignToEstate/{expertId}")
	public String getAssignExpertToEstatePage(@PathVariable("expertId") long expertId, Model model) {
		Optional<User> expertOpt = expertService.findById(expertId);
		
		if(!expertOpt.isPresent()) {
			return "redirect:/experts?wrongId";
		}
		
		User expert = expertOpt.get();
		
		List<Estate> estates = estateService.findAll().stream().filter(e -> !e.getExperts().contains(expert)).collect(Collectors.toList());
		 
		model.addAttribute("choice", new EstateChoiceDTO(expertId));
		model.addAttribute("estates", estates);
		model.addAttribute("validated", false);
		
		return "/expert/assignToEstate";
	}
	
	@PostMapping("/assignExpertToEstate")
	public String assignExpertToEstate(@Valid @ModelAttribute("choice") EstateChoiceDTO choiceDto, BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "/expert/assignToEstate";
		}
		
		Optional<User> expertOpt = expertService.findById(choiceDto.getExpertId());
		Optional<Estate> estateOpt = estateService.findById(choiceDto.getEstateId());
		
		if(!expertOpt.isPresent() || !estateOpt.isPresent()) {
			return "/expert/assignToEstate";
		}
		
		User expert = expertOpt.get();
		Estate estate = estateOpt.get();
		
		expert.getExpertEstates().add(estate);
		estate.getExperts().add(expert);
		
		expertService.save(expert);
		
		return "redirect:/experts?assignToEstateSuccess";
		
	}
	
	@GetMapping("/changePasswordPage/{expertId}")
	public String getChangePasswordPage(@PathVariable("expertId") long expertId, Model model) {
		
		Optional<User> expertOpt = expertService.findById(expertId);
		
		if(!expertOpt.isPresent()) {
			return "redirect:/experts?wrongId";
		}
		
		model.addAttribute("passwordChange", new PasswordChangeDTO(expertId));
		model.addAttribute("validated", false);
		
		return "/expert/changePassword";
	}
	
	@PostMapping("/changePassword")
	public String changeExpertPassword(@Valid @ModelAttribute("passwordChange") PasswordChangeDTO passwordChangeDTO, BindingResult bindingResult,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			return "/expert/changePassword";
		}
		
		Optional<User> expertOpt = expertService.findById(passwordChangeDTO.getId());
		if(!expertOpt.isPresent()) {
			return "/expert/changePassword";
		}
		
		User expert = expertOpt.get();
		
		String newPass = passwordChangeDTO.getPassword();
		String reversedUserName = expert.getUserName();
		
		if(newPass.contains(expert.getUserName()) || newPass.contains(reversedUserName)) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "/expert/changePassword";
		}
		
		expertService.changePassword(expert, passwordChangeDTO.getPassword());
		
		return "redirect:/experts?passwordChanged";
		
	}
	

	private User editExpertAccount(User expertToEdit, ExpertEditDTO expertDto, BindingResult bindingResult) {
		User expert = null;
		try {
			expert = expertService.update(expertToEdit, expertDto);
		} catch(UserAlreadyExistsException e) {
			bindingResult.rejectValue(e.isEmailException() ? "email" : "userName", e.isEmailException() ? "message.emailExists" : "message.userNameExists");
			return null;
		}
		return expert;
	}

	private ExpertEditDTO getExpertDtoFromUser(User user) {
		ExpertEditDTO expert = new ExpertEditDTO();
		expert.setId(user.getId());
		expert.setUserName(user.getUserName());
		expert.setFirstName(user.getFirstName());
		expert.setLastName(user.getLastName());
		expert.setEmail(user.getEmail());
		expert.setCity(user.getAddress().getCity());
		expert.setDistrict(user.getAddress().getDistrict());
		expert.setPostCode(user.getAddress().getPostCode());
		expert.setPostCity(user.getAddress().getPostCity());
		expert.setStreet(user.getAddress().getStreet());
		expert.setHouseNumber(user.getAddress().getHouseNumber());
		expert.setApartmentNumber(user.getAddress().getApartmentNumber());
		expert.setPhoneNumber(user.getAddress().getPhoneNumber());
		expert.setSelectedCategoriesFromCheckboxes(user.getCategories().stream().collect(Collectors.toList()));
		return expert;
	}
	
}
