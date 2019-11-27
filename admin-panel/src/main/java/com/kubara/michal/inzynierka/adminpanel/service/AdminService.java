package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kubara.michal.inzynierka.adminpanel.dto.AdminDTO;
import com.kubara.michal.inzynierka.adminpanel.entity.Admin;
import com.kubara.michal.inzynierka.adminpanel.exception.AdminAlreadyExistsException;

public interface AdminService extends UserDetailsService {

	List<Admin> findAll();

	Admin saveAdmin(AdminDTO adminDto) throws AdminAlreadyExistsException;
	
}
