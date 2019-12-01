package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kubara.michal.inzynierka.adminpanel.dto.UserDTO;
import com.kubara.michal.inzynierka.adminpanel.exception.UserAlreadyExistsException;
import com.kubara.michal.inzynierka.core.entity.User;

public interface UserService {

	Page<User> findAll(Pageable pageable);

	Optional<User> findById(long userId);
	
	void delete(User user);
	
	User save(User user);

	void changePassword(User user, String password);

	boolean isUser(User user);

	User saveUser(UserDTO userDto) throws UserAlreadyExistsException;
	
}
