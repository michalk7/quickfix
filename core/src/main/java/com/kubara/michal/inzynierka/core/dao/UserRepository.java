package com.kubara.michal.inzynierka.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.core.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String userName);
	
}
