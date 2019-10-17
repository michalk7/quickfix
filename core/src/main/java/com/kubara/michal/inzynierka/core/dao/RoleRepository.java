package com.kubara.michal.inzynierka.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.core.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);
	
}
