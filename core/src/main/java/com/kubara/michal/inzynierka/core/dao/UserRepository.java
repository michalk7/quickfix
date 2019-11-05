package com.kubara.michal.inzynierka.core.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.core.entity.Category;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.Role;
import com.kubara.michal.inzynierka.core.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String userName);
	
	User findByEmail(String email);
	
	Page<User> findAllByRolesAndEnabledAndVerifiedAndAddressCity(Role role, boolean enabled, boolean verified, String city, Pageable pageable);
	
	//tylko showAll
	Page<User> findAllByRolesAndEnabledAndVerified(Role role, boolean enabled, boolean verified, Pageable pageable);
	
	//z trzema kryteriami showall = nic w zapytaniu
	Page<User> findAllByRolesAndEnabledAndVerifiedAndCategoriesAndExpertEstate(Role role, boolean enabled, boolean verified, Category category, Estate estate, Pageable pageable);
}
