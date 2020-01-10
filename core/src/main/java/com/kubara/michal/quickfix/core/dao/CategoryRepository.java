package com.kubara.michal.quickfix.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.quickfix.core.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByName(String name);
	
}
