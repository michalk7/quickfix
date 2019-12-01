package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.adminpanel.dto.UserDTO;
import com.kubara.michal.inzynierka.adminpanel.exception.UserAlreadyExistsException;
import com.kubara.michal.inzynierka.core.dao.RoleRepository;
import com.kubara.michal.inzynierka.core.dao.UserRepository;
import com.kubara.michal.inzynierka.core.entity.Address;
import com.kubara.michal.inzynierka.core.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAllByRoles(roleRepository.findByName("ROLE_USER"), pageable);
	}

	@Override
	@Transactional
	public Optional<User> findById(long userId) {
		return userRepository.findById(userId);
	}

	@Override
	@Transactional
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public void changePassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

	@Override
	@Transactional
	public boolean isUser(User user) {
		return user.getRoles().contains(roleRepository.findByName("ROLE_USER"));
	}
	
	private boolean emailExists(String email) {
		User user = userRepository.findByEmail(email);
		if(user != null) {
			return true;
		}
		return false;
	}
	
	private boolean userNameExists(String userName) {
		User user = userRepository.findByUserName(userName);
		if(user != null) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public User saveUser(UserDTO userDto) throws UserAlreadyExistsException{
		if(emailExists(userDto.getEmail())) {
			throw new UserAlreadyExistsException("Podany adres email jest już zajęty.", true);
		}
		
		if(userNameExists(userDto.getUserName())) {
			throw new UserAlreadyExistsException("Podana nazwa użytkownika jest już zajęta.", false);
		}


		User user = new User();
		user.setUserName(userDto.getUserName());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		
		Address address = new Address();
		address.setCity(userDto.getCity());
		address.setDistrict(userDto.getDistrict());
		address.setPostCode(userDto.getPostCode());
		address.setPostCity(userDto.getPostCity());
		address.setStreet(userDto.getStreet());
		address.setHouseNumber(userDto.getHouseNumber());
		address.setApartmentNumber(userDto.getApartmentNumber());
		address.setPhoneNumber(userDto.getPhoneNumber());
		
		address.setUser(user);
		user.setAddress(address);
		
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
		
		User result = userRepository.save(user);
		
		return result;
	}

}
