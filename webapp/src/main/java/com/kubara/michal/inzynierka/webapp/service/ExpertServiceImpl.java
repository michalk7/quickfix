package com.kubara.michal.inzynierka.webapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kubara.michal.inzynierka.core.dao.RoleRepository;
import com.kubara.michal.inzynierka.core.dao.UserRepository;
import com.kubara.michal.inzynierka.core.entity.Category;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.User;

@Service
public class ExpertServiceImpl implements ExpertService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public Page<User> findAllByCity(String city, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndAddressCity(roleRepository.findByName("ROLE_EXPERT"), true, true, city, pageable);
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerified(roleRepository.findByName("ROLE_EXPERT"), true, true, pageable);
	}

	@Override
	public Page<User> findAllByCategoryAndEstate(Category category, Estate estate, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndCategoriesAndExpertEstates(
				roleRepository.findByName("ROLE_EXPERT"), true, true, category, estate, pageable);
	}

	@Override
	public Page<User> findAllByCategoryAndEstateAndCity(Category category, Estate estate, String city,
			Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndCategoriesAndExpertEstatesAndAddressCity(
				roleRepository.findByName("ROLE_EXPERT"), true, true, category, estate, city, pageable);
	}

	@Override
	public Page<User> findAllByCategory(Category category, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndCategories(
				roleRepository.findByName("ROLE_EXPERT"), true, true, category, pageable);
	}

	@Override
	public Page<User> findAllByCategoryAndCity(Category category, String city, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndCategoriesAndAddressCity(
				roleRepository.findByName("ROLE_EXPERT"), true, true, category, city, pageable);
	}

	@Override
	public Page<User> findAllByEstate(Estate estate, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndExpertEstates(
				roleRepository.findByName("ROLE_EXPERT"), true, true, estate, pageable);
	}

	@Override
	public Page<User> findAllEstateAndCity(Estate estate, String city, Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerifiedAndExpertEstatesAndAddressCity(
				roleRepository.findByName("ROLE_EXPERT"), true, true, estate, city, pageable);
	}

	@Override
	public Optional<User> findById(long id) {
		return userRepository.findById(id);
	}

	
	
}
