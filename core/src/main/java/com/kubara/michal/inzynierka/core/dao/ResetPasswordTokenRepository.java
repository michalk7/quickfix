package com.kubara.michal.inzynierka.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.core.entity.ResetPasswordToken;
import com.kubara.michal.inzynierka.core.entity.User;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

	ResetPasswordToken findByToken(String token);
	 
	ResetPasswordToken findByUser(User user);
	
}
