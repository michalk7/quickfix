package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kubara.michal.inzynierka.adminpanel.dto.ExpertDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.ExpertEditDTO;
import com.kubara.michal.inzynierka.adminpanel.exception.UserAlreadyExistsException;
import com.kubara.michal.inzynierka.core.entity.User;

public interface ExpertService {

	Page<User> findAll(Pageable pageable);

	Optional<User> findById(long expertId);

	boolean isExpert(User expert);

	void delete(User expert);

	User saveExpert(ExpertDTO expertDto) throws UserAlreadyExistsException;

	User update(User expertToEdit, ExpertEditDTO expertDto) throws UserAlreadyExistsException;

	User save(User expert);
	
}
