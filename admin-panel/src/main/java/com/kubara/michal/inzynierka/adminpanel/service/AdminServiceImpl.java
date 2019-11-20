package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.adminpanel.dao.AdminRepository;
import com.kubara.michal.inzynierka.adminpanel.entity.Admin;
import com.kubara.michal.inzynierka.adminpanel.entity.Role;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminRepository adminRepository;

	@Override
	@Transactional("securityTransactionManager")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Admin admin = adminRepository.findByUserName(username);
		
		if(admin == null) {
			throw new UsernameNotFoundException("Niepoprawna nazwa użytkownika lub hasło.");
		}
		
		return new org.springframework.security.core.userdetails.User(admin.getUserName(), admin.getPassword(),
				mapRolesToAuthorities(admin.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
