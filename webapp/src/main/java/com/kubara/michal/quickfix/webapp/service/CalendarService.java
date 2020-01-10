package com.kubara.michal.quickfix.webapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.kubara.michal.quickfix.core.entity.Event;
import com.kubara.michal.quickfix.core.entity.User;

public interface CalendarService {
	
	List<Event> findAllByDateBetweenAndExpert(LocalDateTime startDate, LocalDateTime endDate, User expert);
	
	List<Event> findAllByDateBetweenAndUser(LocalDateTime startDate, LocalDateTime endDate, User user);

	List<Event> findAllByExpert(User expert);

	List<Event> findAllByUser(User user);
	
	Event save(Event event);
	
	Optional<Event> findById(long id);
	
	void delete(Event event);

	List<Event> findAllByStartDateBetweenAndExpert(LocalDateTime dateStart, LocalDateTime dateEnd, User expert);

	List<Event> findAllByEndDateBetweenAndExpert(LocalDateTime dateStart, LocalDateTime dateEnd, User expert);
	
	List<Event> findAllByNewEventBetweenAndExpert(LocalDateTime dateStart, LocalDateTime dateEnd, User expert);
	
}
