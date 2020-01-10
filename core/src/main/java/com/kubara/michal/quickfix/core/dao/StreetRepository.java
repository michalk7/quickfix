package com.kubara.michal.quickfix.core.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.quickfix.core.entity.Street;

public interface StreetRepository extends JpaRepository<Street, Long> {

	Optional<Street> findByStreetNameAndStreetNumberAndCityAndDistrictAndPostCode(String streetName, String streetNumber, String city, String district, String postCode);
	
}
