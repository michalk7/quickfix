package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.adminpanel.dto.EstateDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.StreetDTO;
import com.kubara.michal.inzynierka.core.dao.EstateRepository;
import com.kubara.michal.inzynierka.core.dao.StreetRepository;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.Street;

@Service
public class EstateServiceImpl implements EstateService {

	@Autowired
	private EstateRepository estateRepository;
	
	@Autowired
	private StreetRepository streetRepository;
	
	@Override
	@Transactional
	public List<Estate> findAll() {
		return estateRepository.findAll();
	}

	@Override
	@Transactional
	public Optional<Estate> findById(long estateId) {
		return estateRepository.findById(estateId);
	}

	@Override
	@Transactional
	public Page<Estate> findAll(Pageable pageable) {
		return estateRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Estate create(EstateDTO estateDto) {
		Estate estate = new Estate();
		estate.setName(estateDto.getName());
		return estateRepository.save(estate);
	}

	@Override
	@Transactional
	public Estate update(Estate estateToEdit, EstateDTO estateDTO) {
		estateToEdit.setName(estateDTO.getName());
		return estateRepository.save(estateToEdit);
	}

	@Override
	@Transactional
	public Street addStreet(StreetDTO streetDto) {
		Optional<Estate> estateOpt = estateRepository.findById(streetDto.getEstateId());
		if(!estateOpt.isPresent()) {
			return null;
		}
		Estate estate = estateOpt.get();
		Street street = new Street();
		street.setStreetName(streetDto.getStreetName());
		street.setStreetNumber(streetDto.getStreetNumber());
		street.setCity(streetDto.getCity());
		street.setDistrict(streetDto.getDistrict());
		street.setPostCode(streetDto.getPostCode());
		street.setEstate(estate);
		estate.getStreets().add(street);
		
		return streetRepository.save(street);
	}

	@Override
	@Transactional
	public Optional<Street> findStreetById(long streetId) {
		return streetRepository.findById(streetId);
	}

	@Override
	@Transactional
	public void deleteStreet(Street street) {
		streetRepository.delete(street);
	}

}
