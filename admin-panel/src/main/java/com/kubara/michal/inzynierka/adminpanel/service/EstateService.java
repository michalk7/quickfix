package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kubara.michal.inzynierka.adminpanel.dto.EstateDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.StreetDTO;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.Street;

public interface EstateService {

	Page<Estate> findAll(Pageable pageable);
	
	List<Estate> findAll();

	Optional<Estate> findById(long estateId);

	Estate create(EstateDTO estateDto);

	Estate update(Estate estateToEdit, EstateDTO estateDTO);

	Street addStreet(StreetDTO streetDto);

	Optional<Street> findStreetById(long streetId);

	void deleteStreet(Street street);
	
}
