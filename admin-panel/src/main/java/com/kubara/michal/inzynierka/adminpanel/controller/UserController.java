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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kubara.michal.inzynierka.adminpanel.dto.AccountStatusDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.PasswordChangeDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.UserDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.UserDetailsDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.UserEditDTO;
import com.kubara.michal.inzynierka.adminpanel.exception.UserAlreadyExistsException;
import com.kubara.michal.inzynierka.adminpanel.service.UserService;
import com.kubara.michal.inzynierka.adminpanel.utils.GenericResponse;
import com.kubara.michal.inzynierka.adminpanel.utils.StringUtils;
import com.kubara.michal.inzynierka.core.entity.User;

@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
	
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping({ "", "/" })
	public String getUsersList(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		
		final int currentPage = page.orElse(1);
		final int pageSize = size.orElse(5);
		
		PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);
		
		Page<User> userPage = userService.findAll(pageRequest);
		
		model.addAttribute("userPage", userPage);
		
		int totalPages = userPage.getTotalPages();
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
		
		return "/user/userList";
		
	}
	
	@GetMapping("details/{userId}")
	@ResponseBody
	public UserDetailsDTO getUserDetails(@PathVariable("userId") long userId) {
		Optional<User> userOpt = userService.findById(userId);
	
		if(!userOpt.isPresent()) {
			return null;
		}
		
		User user = userOpt.get();
				
		UserDetailsDTO userToSend = new UserDetailsDTO(
				user.getAddress().getCity(),
				user.getAddress().getDistrict(),
				user.getAddress().getPostCode(),
				user.getAddress().getPostCity(),
				user.getAddress().getStreet(),
				user.getAddress().getHouseNumber(), 
				user.getAddress().getApartmentNumber(),
				user.getAddress().getPhoneNumber()
			);
		
		return userToSend;
	}
	
	@DeleteMapping("/delete/{userId}")
	@ResponseBody
	public GenericResponse deleteUser(@PathVariable("userId") long userId) {
		
		Optional<User> userOpt = userService.findById(userId);
		
		if(!userOpt.isPresent()) {
			return new GenericResponse("Brak użytkownika o podanym id", "Wrong ID");
		}
		
		User user = userOpt.get();
		
		if(!userService.isUser(user)) {
			return new GenericResponse("Użytkownik nie jest klientem", "Wrong User type");
		}
		
		userService.delete(user);
		
		return new GenericResponse("Pomyślnie usunięto klienta");
		
	}
	
	@PutMapping("/changeAccountStatus/{userId}")
	@ResponseBody
	public GenericResponse changeAccountStatus(@PathVariable("userId") long userId, @RequestBody AccountStatusDTO accountStatusDTO) {
		Optional<User> userOpt = userService.findById(userId);
		
		if(!userOpt.isPresent()) {
			return new GenericResponse("Brak użytkownika o podanym id", "Wrong ID");
		}
		
		User user = userOpt.get();
		
		user.setEnabled(accountStatusDTO.isEnabled());
		User savedUser = userService.save(user);
		
		if(savedUser == null) {
			return new GenericResponse("Błąd zapisu", "Save Error");
		}
		
		return new GenericResponse("Zmieniono status konta użytkownika");
		
	}
	
	@GetMapping("/add")
	public String getAddUserPage(Model model) {
		model.addAttribute("user", new UserDTO());
		model.addAttribute("validated", false);
		
		return "/user/addUser";
	}
	
	@PostMapping("/add")
	public String addNewUser(@Valid @ModelAttribute("user") UserDTO userDto, 
			BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "/user/addUser";
		}
		
		String reversedUserName = StringUtils.reverseString(userDto.getUserName());
		
		String newPass = userDto.getPassword();
		
		if(newPass.contains(userDto.getUserName()) || newPass.contains(reversedUserName)) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "/user/addUser";
		}
		
		User newUser = createNewUserAccount(userDto, bindingResult);
		if(newUser == null) {
			return "/user/addUser";
		}
		
		return "redirect:/users?addSuccess";
		
	}

	private User createNewUserAccount(UserDTO userDto, BindingResult bindingResult) {
		User newUser = null;
		try {
			newUser = userService.saveUser(userDto);
		} catch(UserAlreadyExistsException e) {
			bindingResult.rejectValue(e.isEmailException() ? "email" : "userName", e.isEmailException() ? "message.emailExists" : "message.userNameExists");
			return null;
		}
		
		return newUser;
	}
	
	@GetMapping("/edit/{userId}")
	public String getEditPage(@PathVariable("userId") long userId, Model model) {
		Optional<User> userOpt = userService.findById(userId);
		
		if(!userOpt.isPresent()) {
			return "redirect:/users?wrongId";
		}
		
		model.addAttribute("user", getUserDtoFromUser(userOpt.get()));
		model.addAttribute("validated", false);
		
		return "/user/editUser";
	}
	
	@PutMapping("/editUserData")
	public String editUserData(@Valid @ModelAttribute("expert") UserEditDTO userDto, 
			BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "/user/editUser";
		}
		
		Optional<User> userOpt = userService.findById(userDto.getId());
		if(!userOpt.isPresent()) {
			return "/user/editUser";
		}
		
		User editedUser = editUserAccount(userOpt.get(), userDto, bindingResult);
		
		if(editedUser == null) {
			return "/user/editUser";
		}
		
		return "redirect:/users?editSuccess";
		
	}

	private User editUserAccount(User userToEdit, UserEditDTO userDto, BindingResult bindingResult) {
		User user = null;
		try {
			user = userService.update(userToEdit, userDto);
		} catch(UserAlreadyExistsException e) {
			bindingResult.rejectValue(e.isEmailException() ? "email" : "userName", e.isEmailException() ? "message.emailExists" : "message.userNameExists");
			return null;
		}
		return user;
	}
	
	@GetMapping("/changePasswordPage/{userId}")
	public String getChangePasswordPage(@PathVariable("userId") long userId, Model model) {
		
		Optional<User> userOpt = userService.findById(userId);
		
		if(!userOpt.isPresent()) {
			return "redirect:/users?wrongId";
		}
		
		model.addAttribute("passwordChange", new PasswordChangeDTO(userId));
		model.addAttribute("validated", false);
		
		return "/user/changePassword";
	}
	
	@PostMapping("/changePassword")
	public String changeExpertPassword(@Valid @ModelAttribute("passwordChange") PasswordChangeDTO passwordChangeDTO, BindingResult bindingResult,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			return "/user/changePassword";
		}
		
		Optional<User> userOpt = userService.findById(passwordChangeDTO.getId());
		if(!userOpt.isPresent()) {
			return "/user/changePassword";
		}
		
		User user = userOpt.get();
		
		String newPass = passwordChangeDTO.getPassword();
		String reversedUserName = user.getUserName();
		
		if(newPass.contains(user.getUserName()) || newPass.contains(reversedUserName)) {
			bindingResult.rejectValue("password", "message.passwordContainsUsername");
			return "/user/changePassword";
		}
		
		userService.changePassword(user, passwordChangeDTO.getPassword());
		
		return "redirect:/users?passwordChanged";
		
	}

	private UserEditDTO getUserDtoFromUser(User user) {
		UserEditDTO userDto = new UserEditDTO();
		userDto.setId(user.getId());
		userDto.setUserName(user.getUserName());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setCity(user.getAddress().getCity());
		userDto.setDistrict(user.getAddress().getDistrict());
		userDto.setPostCode(user.getAddress().getPostCode());
		userDto.setPostCity(user.getAddress().getPostCity());
		userDto.setStreet(user.getAddress().getStreet());
		userDto.setHouseNumber(user.getAddress().getHouseNumber());
		userDto.setApartmentNumber(user.getAddress().getApartmentNumber());
		userDto.setPhoneNumber(user.getAddress().getPhoneNumber());
		
		return userDto;
	}
	
}
