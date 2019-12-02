package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kubara.michal.inzynierka.core.entity.Estate;

public interface EstateService {

	Page<Estate> findAll(Pageable pageable);
	
	List<Estate> findAll();

	Optional<Estate> findById(long estateId);
	
}
