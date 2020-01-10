package com.kubara.michal.quickfix.webapp.service;

import java.util.Arrays;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kubara.michal.quickfix.core.dao.ResetPasswordTokenRepository;
import com.kubara.michal.quickfix.core.entity.ResetPasswordToken;
import com.kubara.michal.quickfix.core.entity.User;

@Service
public class UserValidationServiceImpl implements UserValidationService {
	
	@Autowired
	private ResetPasswordTokenRepository passwordTokenRepository;

	@Override
	public String validateResetPasswordToken(long id, String token) {
		final ResetPasswordToken passwordToken = passwordTokenRepository.findByToken(token);
		if((passwordToken == null) || (passwordToken.getUser().getId() != id) ) {
			return "invalidToken";
		}
		
		final Calendar cal = Calendar.getInstance();
		if((passwordToken.getExpirationDate().getTime() - cal.getTime().getTime()) <= 0) {
			return "expiredToken";
		}
		
		final User user = passwordToken.getUser();
		final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		return null;
	}

}
