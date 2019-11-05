package com.kubara.michal.inzynierka.core.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.core.entity.Street;

public interface StreetRepository extends JpaRepository<Street, Long> {

	Optional<Street> findByStreetNameAndStreetNumber(String streetName, String streetNumber);
	
}
