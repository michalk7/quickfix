package com.kubara.michal.quickfix.adminpanel.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.kubara.michal.quickfix.adminpanel.dto.AdminDTO;
import com.kubara.michal.quickfix.adminpanel.dto.AdminEditDTO;
import com.kubara.michal.quickfix.adminpanel.dto.PasswordChangeDTO;
import com.kubara.michal.quickfix.adminpanel.entity.Admin;
import com.kubara.michal.quickfix.adminpanel.exception.AdminAlreadyExistsException;
import com.kubara.michal.quickfix.adminpanel.service.AdminService;
import com.kubara.michal.quickfix.adminpanel.utils.GenericResponse;
import com.kubara.michal.quickfix.adminpanel.utils.StringUtils;

@Controller
@RequestMapping("/admins")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
	
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping({ "", "/" })
	public String getAdminList(Model model) {
		
		List<Admin> adminList = adminService.findAll();
		
		model.addAttribute("admins", adminList);
		
		return "/admin/adminList";
	}
	
	@GetMapping("/add")
	public String getAddPage(Model model) {
		
		model.addAttribute("admin", new AdminDTO());
		model.addAttribute("validated", false);
		
		return "/admin/addAdmin";
	}
	
	@PostMapping("/add")
	public String addNewAdmin(@Valid @ModelAttribute("admin") AdminDTO adminDto, BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "/admin/addAdmin";
		}
		
		String reversedUserName = StringUtils.reverseString(adminDto.getUserName());
		
		String newPass = adminDto.getPassword();
		
		if( newPass.contains(adminDto.getUserName()) || newPass.contains(reversedUserName)) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "/admin/addAdmin";
		}
		
		Admin newAdmin = createNewAdminAccount(adminDto, bindingResult);
		if(newAdmin == null) {
			return "/admin/addAdmin";
		}
		
		return "redirect:/admins?addSuccess";
		
	}
	
	@GetMapping("/edit/{adminId}")
	public String getEditPage(@PathVariable("adminId") long adminId, Model model) {
		Optional<Admin> adminOpt = adminService.findById(adminId);
		
		if(!adminOpt.isPresent()) {
			return "redirect:/admins?wrongId";
		}
		
		model.addAttribute("admin", getAdminDtoFromAdmin(adminOpt.get()));
		model.addAttribute("validated", false);
		
		return "/admin/editAdmin";
		
	}
	
	@PutMapping("/editAdmin")
	public String editAdmin(@Valid @ModelAttribute("admin") AdminEditDTO adminEditDTO, 
			BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "/admin/editAdmin";
		}
		
		Optional<Admin> adminOpt = adminService.findById(adminEditDTO.getId());
		if(!adminOpt.isPresent()) {
			return "/admin/editAdmin";
		}
		
		Admin editedAdmin = editAdminAccount(adminOpt.get(), adminEditDTO, bindingResult);
		
		if(editedAdmin == null) {
			return "/admin/editAdmin";
		}
		
		return "redirect:/admins?editSuccess";
		
	}
	
	@DeleteMapping("/delete/{adminId}")
	@ResponseBody
	public GenericResponse deleteAdmin(@PathVariable("adminId") long adminId) {
		Optional<Admin> adminOpt = adminService.findById(adminId);
		if(!adminOpt.isPresent()) {
			return new GenericResponse("Brak użytkownika o podanym id", "Wrong ID");
		}
		
		Admin admin = adminOpt.get();
		
		adminService.delete(admin);
		
		return new GenericResponse("Pomyślnie usunięto administratora");
		
	}
	
	@GetMapping("/changePasswordPage/{adminId}")
	public String getChangePasswordPage(@PathVariable("adminId") long adminId, Model model) {
		Optional<Admin> adminOpt = adminService.findById(adminId);
		
		if(!adminOpt.isPresent()) {
			return "redirect:/admins?wrongId";
		}
		
		model.addAttribute("passwordChange", new PasswordChangeDTO(adminId));
		model.addAttribute("validated", false);
		
		return "/admin/changePassword";
	}
	
	@PostMapping("/changePassword")
	public String changeAdminPassword(@Valid @ModelAttribute("passwordChange") PasswordChangeDTO passwordChangeDTO, 
		BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "/admin/changePassword";
		}
		
		Optional<Admin> adminOpt = adminService.findById(passwordChangeDTO.getId());
		if(!adminOpt.isPresent()) {
			return "/admin/changePassword";
		}
		
		Admin admin = adminOpt.get();
		
		String newPassword = passwordChangeDTO.getPassword();
		String reversedUserName = admin.getUserName();
		
		if(newPassword.contains(admin.getUserName()) || newPassword.contains(reversedUserName) ) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "/admin/changePassword";
		}
		
		adminService.changePassword(admin, passwordChangeDTO.getPassword());
		
		return "redirect:/admins?passwordChanged";
		
	}
	

	private Admin editAdminAccount(Admin adminToEdit, AdminEditDTO adminEditDTO, BindingResult bindingResult) {
		Admin admin = null;
		try {
			admin = adminService.update(adminToEdit, adminEditDTO);
		} catch(AdminAlreadyExistsException e) {
			bindingResult.rejectValue(e.isEmailException() ? "email" : "userName", e.isEmailException() ? "message.emailExists" : "message.userNameExists");
			return null;
		}
		return admin;
	}

	private AdminEditDTO getAdminDtoFromAdmin(Admin admin) {
		AdminEditDTO adminDto = new AdminEditDTO();
		adminDto.setId(admin.getId());
		adminDto.setUserName(admin.getUserName());
		adminDto.setFirstName(admin.getFirstName());
		adminDto.setLastName(admin.getLastName());
		adminDto.setEmail(admin.getEmail());
		
		return adminDto;
	}

	private Admin createNewAdminAccount(AdminDTO adminDto, BindingResult bindingResult) {
		Admin newAdmin = null;
		try {
			newAdmin = adminService.saveAdmin(adminDto);
		} catch(AdminAlreadyExistsException e) {
			bindingResult.rejectValue(e.isEmailException() ? "email" : "userName", e.isEmailException() ? "message.emailExists" : "message.userNameExists");
			return null;
		}
		return newAdmin;
	}
	
	
}
