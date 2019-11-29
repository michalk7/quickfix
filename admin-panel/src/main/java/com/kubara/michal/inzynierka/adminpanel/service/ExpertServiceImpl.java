package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.core.dao.RoleRepository;
import com.kubara.michal.inzynierka.core.dao.UserRepository;
import com.kubara.michal.inzynierka.core.entity.User;

@Service
public class ExpertServiceImpl implements ExpertService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAllByRoles(roleRepository.findByName("ROLE_EXPERT"), pageable);
	}

	@Override
	@Transactional
	public Optional<User> findById(long expertId) {
		return userRepository.findById(expertId);
	}
	
	

}
