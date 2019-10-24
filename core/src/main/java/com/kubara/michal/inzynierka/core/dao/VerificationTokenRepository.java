package com.kubara.michal.inzynierka.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.core.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	VerificationToken findByToken(String token);
	 
    VerificationToken findByUser(User user);
	
}
