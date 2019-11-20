package com.kubara.michal.inzynierka.adminpanel.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.adminpanel.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Admin findByUserName(String userName);
	
	Admin findByEmail(String email);
	
}
