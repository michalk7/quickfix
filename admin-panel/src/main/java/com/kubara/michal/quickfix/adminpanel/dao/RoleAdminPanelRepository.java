package com.kubara.michal.quickfix.adminpanel.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.quickfix.adminpanel.entity.Role;

public interface RoleAdminPanelRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);
	
}
