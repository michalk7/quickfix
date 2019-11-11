package com.kubara.michal.inzynierka.webapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.core.dao.EventRepository;
import com.kubara.michal.inzynierka.core.entity.Event;
import com.kubara.michal.inzynierka.core.entity.User;

@Service
public class CalendarServiceImpl implements CalendarService {
	
	@Autowired
	private EventRepository eventRepository;

	@Override
	@Transactional
	public List<Event> findAllByExpert(User expert) {
		return eventRepository.findAllByExpert(expert);
	}

	@Override
	@Transactional
	public List<Event> findAllByUser(User user) {
		return eventRepository.findAllByUser(user);
	}

	@Override
	@Transactional
	public List<Event> findAllByDateBetweenAndExpert(LocalDateTime startDate, LocalDateTime endDate, User expert) {
		return eventRepository.findAllByDateBetweenAndExpert(startDate, endDate, expert);
	}

	@Override
	@Transactional
	public List<Event> findAllByDateBetweenAndUser(LocalDateTime startDate, LocalDateTime endDate, User user) {
		return eventRepository.findAllByDateBetweenAndUser(startDate, endDate, user);
	}

	@Override
	public Event save(Event event) {
		return eventRepository.save(event);
	}

	@Override
	public Optional<Event> findById(long id) {
		return eventRepository.findById(id);
	}

	@Override
	public void delete(Event event) {
		eventRepository.delete(event);
	}

}
