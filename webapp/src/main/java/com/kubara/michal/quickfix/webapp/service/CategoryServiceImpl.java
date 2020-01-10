package com.kubara.michal.quickfix.webapp.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kubara.michal.quickfix.core.dao.CategoryRepository;
import com.kubara.michal.quickfix.core.entity.Category;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	@Transactional
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	@Transactional
	public Optional<Category> findById(long id) {
		return categoryRepository.findById(id);
	}

	
	
}
