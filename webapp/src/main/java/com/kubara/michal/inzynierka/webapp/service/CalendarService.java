package com.kubara.michal.inzynierka.webapp.service;

import java.time.LocalDate;
import java.util.List;

import com.kubara.michal.inzynierka.core.entity.Event;
import com.kubara.michal.inzynierka.core.entity.User;

public interface CalendarService {

	List<Event> findAllByDateAndExpert(LocalDate date, User expert);
	
	List<Event> findAllByDateAndUser(LocalDate date, User user);
	
}
