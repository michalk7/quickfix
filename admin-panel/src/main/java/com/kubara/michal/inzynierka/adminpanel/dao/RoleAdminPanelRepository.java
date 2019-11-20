package com.kubara.michal.inzynierka.adminpanel.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.adminpanel.entity.Role;

public interface RoleAdminPanelRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);
	
}
