package com.kubara.michal.inzynierka.webapp.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kubara.michal.inzynierka.core.entity.Category;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.User;

public interface ExpertService {

	Page<User> findAll(Pageable pageable);

	Page<User> findAllByCity(String city, Pageable pageable);
	
	Page<User> findAllByCategoryAndEstate(Category category, Estate estate, Pageable pageable);
	
	Page<User> findAllByCategoryAndEstateAndCity(Category category, Estate estate, String city, Pageable pageable);
	
	Page<User> findAllByCategory(Category category, Pageable pageable);
	
	Page<User> findAllByCategoryAndCity(Category category, String city, Pageable pageable);
	
	Page<User> findAllByEstate(Estate estate, Pageable pageable);
	
	Page<User> findAllEstateAndCity(Estate estate, String city, Pageable pageable);
	
	Optional<User> findById(long id);
	
}
