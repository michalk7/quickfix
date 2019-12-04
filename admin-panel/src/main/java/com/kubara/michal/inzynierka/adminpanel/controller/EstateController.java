package com.kubara.michal.inzynierka.adminpanel.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kubara.michal.inzynierka.adminpanel.dto.EstateDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.StreetDTO;
import com.kubara.michal.inzynierka.adminpanel.service.EstateService;
import com.kubara.michal.inzynierka.adminpanel.service.ExpertService;
import com.kubara.michal.inzynierka.adminpanel.utils.GenericResponse;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.Street;
import com.kubara.michal.inzynierka.core.entity.User;

@Controller
@RequestMapping("/estates")
public class EstateController {
	
	@Autowired
	private EstateService estateService;
	
	@Autowired
	private ExpertService expertService;

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
		model.addAttribute("estateId", estateId);
		model.addAttribute("streets", streets);
		model.addAttribute("experts", experts);
		model.addAttribute("users", users);
		
		return "/estate/estateDetails";
	}
	
	@GetMapping("/add")
	public String getAddEstatePage(Model model) {
		model.addAttribute("estate", new EstateDTO());
		model.addAttribute("validated", false);
		
		return "/estate/addEstate";
	}
	
	@PostMapping("/add")
	public String addNewEstate(@Valid @ModelAttribute("estate") EstateDTO estateDto, 
			BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "/estate/addEstate";
		}
		
		Estate estate = estateService.create(estateDto);
		if(estate == null) {
			return "/estate/addEstate";
		}
		
		return "redirect:/estates?addSuccess";
	}
	
	@GetMapping("/edit/{estateId}")
	public String getEditPage(@PathVariable("estateId") long estateId, Model model) {
		Optional<Estate> estateOpt = estateService.findById(estateId);
		
		if(!estateOpt.isPresent()) {
			return "redirect:/estates?wrongId";
		}
		
		Estate estate = estateOpt.get();
		EstateDTO estateDto = new EstateDTO();
		estateDto.setName(estate.getName());
		estateDto.setId(estateId);
		
		model.addAttribute("estate", estateDto);
		model.addAttribute("validated", false);
		
		return "/estate/editEstate";
	}
	
	@PutMapping("/editEstate")
	public String editEstate(@Valid @ModelAttribute("estate") EstateDTO estateDTO,
			BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "/estate/editEstate";
		}
		
		Optional<Estate> estateOpt = estateService.findById(estateDTO.getId());
		if(!estateOpt.isPresent()) {
			model.addAttribute("editError", "Niepoprawne id osiedla");
			return "/estate/editEstate";
		}
		Estate estate = estateOpt.get();
		Estate editedEstate = estateService.update(estate, estateDTO);
		
		if(editedEstate == null) {
			model.addAttribute("editError", "Operacja nie powiodła się");
			return "/estate/editEstate";
		}
		
		return "redirect:/estates?editSuccess";
		
	}
	
	@GetMapping("/addStreet/{estateId}")
	public String getAddStreetPage(@PathVariable("estateId") long estateId, Model model) {
		Optional<Estate> estateOpt = estateService.findById(estateId);
		
		if(!estateOpt.isPresent()) {
			return "redirect:/estates?wrongId";
		}
		
		StreetDTO streetDto = new StreetDTO();
		streetDto.setEstateId(estateId);
		
		model.addAttribute("street", streetDto);
		model.addAttribute("validated", false);
		
		return "/estate/addStreet";
		
	}
	
	@PostMapping("/addStreet")
	public String addNewStreet(@Valid @ModelAttribute("street") StreetDTO streetDto,
			BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "/estate/addStreet";
		}
		
		Street street = estateService.addStreet(streetDto);
		if(street == null) {
			return "/estate/addStreet";
		}
		
		return "redirect:/estates?addStreetSuccess";
		
	}
	
	@DeleteMapping("/deleteStreet/{estateId}/{streetId}")
	@ResponseBody
	public GenericResponse deleteStreet(@PathVariable("estateId") long estateId, @PathVariable("streetId") long streetId, 
			Model model ) {
		Optional<Estate> estateOpt = estateService.findById(estateId);
		Optional<Street> streetOpt = estateService.findStreetById(streetId);
		
		if(!estateOpt.isPresent() || !streetOpt.isPresent()) {
			return new GenericResponse("Błędne id osiedla lub ulicy", "Wrong ID");
		}
		
		Street street = streetOpt.get();
		Estate estate = estateOpt.get();
		
		estateService.deleteStreet(street, estate);
		
		return new GenericResponse("Ulica usunięta pomyślnie.");
	}
	
	@DeleteMapping("/deleteExpert/{estateId}/{expertId}")
	@ResponseBody
	public GenericResponse deleteExpert(@PathVariable("estateId") long estateId, @PathVariable("expertId") long expertId, 
			Model model ) {
		Optional<Estate> estateOpt = estateService.findById(estateId);
		Optional<User> expertOpt = expertService.findById(expertId);
		
		if(!estateOpt.isPresent() || !expertOpt.isPresent()) {
			return new GenericResponse("Błędne id osiedla lub fachowca", "Wrong ID");
		}
		
		Estate estate = estateOpt.get();
		User expert = expertOpt.get();
		
		estateService.deleteExpert(estate, expert);
		
		return new GenericResponse("Fachowiec został usunięty z obsługi tego osiedla");
		
	}
	
	@DeleteMapping("/delete/{estateId}")
	@ResponseBody
	public GenericResponse deleteEstate(@PathVariable("estateId") long estateId, Model model ) {
		Optional<Estate> estateOpt = estateService.findById(estateId);
		
		if(!estateOpt.isPresent()) {
			return new GenericResponse("Błędne id osiedla", "Wrong ID");
		}
		
		Estate estate = estateOpt.get();
		
		estateService.delete(estate);
		
		return new GenericResponse("Osiedle zostało usunięte.");	
	}
	
}
