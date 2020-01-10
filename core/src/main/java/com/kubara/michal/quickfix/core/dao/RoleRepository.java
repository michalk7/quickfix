package com.kubara.michal.quickfix.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.quickfix.core.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);
	
}
