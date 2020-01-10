package com.kubara.michal.quickfix.webapp.service;

import java.util.List;
import java.util.Optional;

import com.kubara.michal.quickfix.core.entity.Category;

public interface CategoryService {
	
	List<Category> findAll();

	Optional<Category> findById(long id);
}
