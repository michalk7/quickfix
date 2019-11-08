package com.kubara.michal.inzynierka.webapp.service;

import java.time.LocalDateTime;
import java.util.List;

import com.kubara.michal.inzynierka.core.entity.Event;
import com.kubara.michal.inzynierka.core.entity.User;

public interface CalendarService {
	
	List<Event> findAllByDateBetweenAndExpert(LocalDateTime startDate, LocalDateTime endDate, User expert);
	
	List<Event> findAllByDateBetweenAndUser(LocalDateTime startDate, LocalDateTime endDate, User user);

	List<Event> findAllByExpert(User expert);

	List<Event> findAllByUser(User user);
	
}
