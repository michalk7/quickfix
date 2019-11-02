package com.kubara.michal.inzynierka.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.core.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByName(String name);
	
}
