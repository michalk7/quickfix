package com.kubara.michal.quickfix.webapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.quickfix.core.dao.RoleRepository;
import com.kubara.michal.quickfix.core.dao.UserRepository;
import com.kubara.michal.quickfix.core.entity.Category;
import com.kubara.michal.quickfix.core.entity.Estate;
import com.kubara.michal.quickfix.core.entity.User;

@Service
public class ExpertServiceImpl implements ExpertService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Override
	@Transactional
	public Page<User> findAllByCity(String city, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndAddressCity(roleRepository.findByName("ROLE_EXPERT"), true, true, city, pageable);
	}

	@Override
	@Transactional
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerified(roleRepository.findByName("ROLE_EXPERT"), true, true, pageable);
	}

	@Override
	@Transactional
	public Page<User> findAllByCategoryAndEstate(Category category, Estate estate, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndCategoriesAndExpertEstates(
				roleRepository.findByName("ROLE_EXPERT"), true, true, category, estate, pageable);
	}

	@Override
	@Transactional
	public Page<User> findAllByCategoryAndEstateAndCity(Category category, Estate estate, String city,
			Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndCategoriesAndExpertEstatesAndAddressCity(
				roleRepository.findByName("ROLE_EXPERT"), true, true, category, estate, city, pageable);
	}

	@Override
	@Transactional
	public Page<User> findAllByCategory(Category category, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndCategories(
				roleRepository.findByName("ROLE_EXPERT"), true, true, category, pageable);
	}

	@Override
	@Transactional
	public Page<User> findAllByCategoryAndCity(Category category, String city, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndCategoriesAndAddressCity(
				roleRepository.findByName("ROLE_EXPERT"), true, true, category, city, pageable);
	}

	@Override
	@Transactional
	public Page<User> findAllByEstate(Estate estate, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndExpertEstates(
				roleRepository.findByName("ROLE_EXPERT"), true, true, estate, pageable);
	}

	@Override
	@Transactional
	public Page<User> findAllEstateAndCity(Estate estate, String city, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndExpertEstatesAndAddressCity(
				roleRepository.findByName("ROLE_EXPERT"), true, true, estate, city, pageable);
	}

	@Override
	@Transactional
	public Optional<User> findById(long id) {
		return userRepository.findById(id);
	}

	
	
}
