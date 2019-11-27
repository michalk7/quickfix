package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.adminpanel.dao.AdminRepository;
import com.kubara.michal.inzynierka.adminpanel.dao.RoleAdminPanelRepository;
import com.kubara.michal.inzynierka.adminpanel.dto.AdminDTO;
import com.kubara.michal.inzynierka.adminpanel.entity.Admin;
import com.kubara.michal.inzynierka.adminpanel.entity.Role;
import com.kubara.michal.inzynierka.adminpanel.exception.AdminAlreadyExistsException;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private RoleAdminPanelRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

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

	@Override
	@Transactional("securityTransactionManager")
	public List<Admin> findAll() {
		return adminRepository.findAll();
	}
	
	private boolean emailExists(String email) {
		Admin user = adminRepository.findByEmail(email);
		if(user != null) {
			return true;
		}
		return false;
	}
	
	private boolean userNameExists(String userName) {
		Admin user = adminRepository.findByUserName(userName);
		if(user != null) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional("securityTransactionManager")
	public Admin saveAdmin(AdminDTO adminDto) throws AdminAlreadyExistsException {
		if(emailExists(adminDto.getEmail())) {
			throw new AdminAlreadyExistsException("Podany adres email jest już zajęty.", true);
		}
		
		if(userNameExists(adminDto.getUserName())) {
			throw new AdminAlreadyExistsException("Podana nazwa użytkownika jest już zajęta.", false);
		}
		
		Admin admin = new Admin();
		admin.setUserName(adminDto.getUserName());
		admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
		admin.setFirstName(adminDto.getFirstName());
		admin.setLastName(adminDto.getLastName());
		admin.setEmail(adminDto.getEmail());
		
		admin.setRoles(Arrays.asList(roleRepository.findByName("ROLE_ADMIN")));
		
		Admin result = adminRepository.save(admin);
		
		return result;
	}

}
