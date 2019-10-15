package com.kubara.michal.inzynierka.webapp.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kubara.michal.inzynierka.webapp.dto.UserDTO;
import com.kubara.michal.inzynierka.webapp.entity.User;

public interface UserService extends UserDetailsService {
	
	public User findByUserName(String userName);

	public void save(UserDTO userDTO, String roleName);

}
