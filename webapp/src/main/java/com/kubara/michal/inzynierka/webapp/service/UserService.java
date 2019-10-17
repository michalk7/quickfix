package com.kubara.michal.inzynierka.webapp.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.webapp.dto.UserDTO;

public interface UserService extends UserDetailsService {
	
	public User findByUserName(String userName);

	public void save(UserDTO userDTO, String roleName);

}
