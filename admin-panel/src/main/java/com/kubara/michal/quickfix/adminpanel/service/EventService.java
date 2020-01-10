package com.kubara.michal.quickfix.adminpanel.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kubara.michal.quickfix.core.entity.Event;

public interface EventService {

	Page<Event> findAll(Pageable pageable);
	
}
