package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kubara.michal.inzynierka.adminpanel.dto.AdminDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.AdminEditDTO;
import com.kubara.michal.inzynierka.adminpanel.entity.Admin;
import com.kubara.michal.inzynierka.adminpanel.exception.AdminAlreadyExistsException;

public interface AdminService extends UserDetailsService {

	List<Admin> findAll();

	Admin saveAdmin(AdminDTO adminDto) throws AdminAlreadyExistsException;

	Optional<Admin> findById(long adminId);

	Admin update(Admin adminToEdit, AdminEditDTO adminEditDTO) throws AdminAlreadyExistsException;

	void delete(Admin admin);

	void changePassword(Admin admin, String password);
	
}
