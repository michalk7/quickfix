package com.kubara.michal.inzynierka.adminpanel.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kubara.michal.inzynierka.core.entity.User;

public interface ExpertService {

	Page<User> findAll(Pageable pageable);
	
}
