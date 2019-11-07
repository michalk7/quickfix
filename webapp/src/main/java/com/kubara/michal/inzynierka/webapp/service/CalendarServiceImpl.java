package com.kubara.michal.inzynierka.webapp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kubara.michal.inzynierka.core.dao.EventRepository;
import com.kubara.michal.inzynierka.core.entity.Event;
import com.kubara.michal.inzynierka.core.entity.User;

@Service
public class CalendarServiceImpl implements CalendarService {
	
	@Autowired
	private EventRepository eventRepository;

	@Override
	public List<Event> findAllByDateAndExpert(LocalDate date, User expert) {
		return eventRepository.findAllByDateAndExpert(date, expert);
	}

	@Override
	public List<Event> findAllByDateAndUser(LocalDate date, User user) {
		return eventRepository.findAllByDateAndUser(date, user);
	}

}
