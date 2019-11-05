package com.kubara.michal.inzynierka.webapp.service;

import java.util.List;
import java.util.Optional;

import com.kubara.michal.inzynierka.core.entity.Category;

public interface CategoryService {
	
	List<Category> findAll();

	Optional<Category> findById(long id);
}
