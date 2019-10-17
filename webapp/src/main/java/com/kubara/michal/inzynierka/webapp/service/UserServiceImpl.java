package com.kubara.michal.inzynierka.webapp.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.core.dao.RoleRepository;
import com.kubara.michal.inzynierka.core.dao.UserRepository;
import com.kubara.michal.inzynierka.core.entity.Address;
import com.kubara.michal.inzynierka.core.entity.Role;
import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.webapp.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUserName(username);
		if(user == null) {
			throw new UsernameNotFoundException("Niepoprawna nazwa użytkownika lub hasło.");
		}
		
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Override
	@Transactional
	public void save(UserDTO userDTO, String roleName) {
		User user = new User();
		user.setUserName(userDTO.getUserName());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		
		Address address = new Address();
		address.setCity(userDTO.getCity());
		address.setPostCode(userDTO.getPostCode());
		address.setPostCity(userDTO.getPostCity());
		address.setStreet(userDTO.getStreet());
		address.setHouseNumber(userDTO.getHouseNumber());
		address.setApartmentNumber(userDTO.getApartmentNumber());
		address.setPhoneNumber(userDTO.getPhoneNumber());
		
		address.setUser(user);
		user.setAddress(address);
		
		user.setRoles(Arrays.asList(roleRepository.findByName(roleName)));
		
		userRepository.save(user);

	}

}
