package com.kubara.michal.inzynierka.adminpanel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.core.dao.EventRepository;
import com.kubara.michal.inzynierka.core.entity.Event;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	private EventRepository eventRepository;

	@Override
	@Transactional
	public Page<Event> findAll(Pageable pageable) {
		return eventRepository.findAll(pageable);
	}

}
