package com.kubara.michal.inzynierka.webapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kubara.michal.inzynierka.core.entity.Category;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.User;

public interface ExpertService {

	Page<User> findAll(Pageable pageable);

	Page<User> findAllByCity(String city, Pageable pageable);
	
	Page<User> findAllByCategoryAndEstate(Category category, Estate estate, Pageable pageable);
	
}
