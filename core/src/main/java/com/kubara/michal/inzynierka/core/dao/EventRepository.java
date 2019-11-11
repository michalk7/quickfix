package com.kubara.michal.inzynierka.core.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kubara.michal.inzynierka.core.entity.Event;
import com.kubara.michal.inzynierka.core.entity.User;

public interface EventRepository extends JpaRepository<Event, Long> {
	
	@Query("select b from Event b where b.startDate >= :startDate and b.endDate <= :endDate and b.expert.id = :#{#expert.id}")
	List<Event> findAllByDateBetweenAndExpert( @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("expert") User expert);
	
	@Query("select b from Event b where b.startDate >= :startDate and b.endDate <= :endDate and b.user.id = :#{#user.id}")
	List<Event> findAllByDateBetweenAndUser(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("user") User user);
	
	@Query("select b from Event b where b.startDate >= :startDate and b.startDate <= :endDate and b.endDate > :endDate and b.expert.id = :#{#expert.id}")
	List<Event> findAllByStartDateBetweenAndExpert( @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("expert") User expert);
	
	@Query("select b from Event b where b.endDate >= :startDate and b.endDate <= :endDate and b.startDate < :startDate and b.expert.id = :#{#expert.id}")
	List<Event> findAllByEndDateBetweenAndExpert( @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("expert") User expert);
	
	@Query("select b from Event b where b.endDate > :endDate and b.startDate < :startDate and b.expert.id = :#{#expert.id}")
	List<Event> findAllByNewEventBetweenAndExpert( @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("expert") User expert);
	
	List<Event> findAllByExpert(User expert);

	List<Event> findAllByUser(User user);

}
