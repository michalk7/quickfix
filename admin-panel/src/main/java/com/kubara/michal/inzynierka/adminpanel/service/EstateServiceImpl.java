package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.core.dao.EstateRepository;
import com.kubara.michal.inzynierka.core.entity.Estate;

@Service
public class EstateServiceImpl implements EstateService {

	@Autowired
	private EstateRepository estateRepository;
	
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

}
