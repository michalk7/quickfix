package com.kubara.michal.inzynierka.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kubara.michal.inzynierka.core.dao.RoleRepository;
import com.kubara.michal.inzynierka.core.dao.UserRepository;
import com.kubara.michal.inzynierka.core.entity.User;

@Service
public class ExpertServiceImpl implements ExpertService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAllByRolesAndEnabledAndVerified(roleRepository.findByName("ROLE_EXPERT"), true, true, pageable);
	}

}
