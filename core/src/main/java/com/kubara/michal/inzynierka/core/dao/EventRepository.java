package com.kubara.michal.inzynierka.core.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.core.entity.Event;
import com.kubara.michal.inzynierka.core.entity.User;

public interface EventRepository extends JpaRepository<Event, Long> {
	
	List<Event> findAllByDateBetweenAndExpert(LocalDate startDate, LocalDate endDate, User expert);
	
	List<Event> findAllByDateAndExpert(LocalDate date, User expert);
	
	List<Event> findAllByDateAndUser(LocalDate date, User user);

}
