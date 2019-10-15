package com.kubara.michal.inzynierka.webapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.webapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String userName);
	
}
