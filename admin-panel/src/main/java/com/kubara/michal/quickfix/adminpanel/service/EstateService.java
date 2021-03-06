package com.kubara.michal.quickfix.adminpanel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kubara.michal.quickfix.adminpanel.dto.EstateDTO;
import com.kubara.michal.quickfix.adminpanel.dto.StreetDTO;
import com.kubara.michal.quickfix.core.entity.Estate;
import com.kubara.michal.quickfix.core.entity.Street;
import com.kubara.michal.quickfix.core.entity.User;

public interface EstateService {

	Page<Estate> findAll(Pageable pageable);
	
	List<Estate> findAll();

	Optional<Estate> findById(long estateId);

	Estate create(EstateDTO estateDto);

	Estate update(Estate estateToEdit, EstateDTO estateDTO);

	Street addStreet(StreetDTO streetDto);

	Optional<Street> findStreetById(long streetId);

	void deleteStreet(Street street, Estate estate);

	void deleteExpert(Estate estate, User expert);

	void delete(Estate estate);
	
}
