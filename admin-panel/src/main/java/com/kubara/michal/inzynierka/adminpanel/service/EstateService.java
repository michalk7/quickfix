package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.List;
import java.util.Optional;

import com.kubara.michal.inzynierka.core.entity.Estate;

public interface EstateService {

	List<Estate> findAll();

	Optional<Estate> findById(long estateId);
	
}
