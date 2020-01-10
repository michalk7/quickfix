package com.kubara.michal.quickfix.core.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.quickfix.core.entity.Category;
import com.kubara.michal.quickfix.core.entity.Estate;
import com.kubara.michal.quickfix.core.entity.Role;
import com.kubara.michal.quickfix.core.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String userName);
	
	User findByEmail(String email);
	
	Page<User> findAllByRoles(Role role, Pageable pageable);
	
	//bez paramsów
	Page<User> findAllByRolesAndEnabledAndVerifiedAndAddressCity(Role role, boolean enabled, boolean verified, String city, Pageable pageable);
	
	//tylko showAll
	Page<User> findAllByRolesAndEnabledAndVerified(Role role, boolean enabled, boolean verified, Pageable pageable);
	
	//kategorie i osiedle
	Page<User> findAllByRolesAndEnabledAndVerifiedAndCategoriesAndExpertEstatesAndAddressCity(Role role, boolean enabled, boolean verified, Category category, Estate estate, String city, Pageable pageable);
	
	//kategorie i show all
	Page<User> findAllByRolesAndEnabledAndVerifiedAndCategories(Role role, boolean enabled, boolean verified, Category category, Pageable pageable);
	
	//moje osiedle i show all
	Page<User> findAllByRolesAndEnabledAndVerifiedAndExpertEstates(Role role, boolean enabled, boolean verified, Estate estate, Pageable pageable);
	
	//moje osiedle
	Page<User> findAllByRolesAndEnabledAndVerifiedAndExpertEstatesAndAddressCity(Role role, boolean enabled, boolean verified, Estate estate, String city, Pageable pageable);
	
	//kategorie
	Page<User> findAllByRolesAndEnabledAndVerifiedAndCategoriesAndAddressCity(Role role, boolean enabled, boolean verified, Category category, String city, Pageable pageable);
	
	//z trzema kryteriami showall = nic w zapytaniu
	Page<User> findAllByRolesAndEnabledAndVerifiedAndCategoriesAndExpertEstates(Role role, boolean enabled, boolean verified, Category category, Estate estate, Pageable pageable);

	List<User> findAllByRolesAndEnabledAndUserEstate(Role role, boolean enabled, Estate userEstate);
	
}
