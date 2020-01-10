package com.kubara.michal.quickfix.adminpanel.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.quickfix.adminpanel.dto.UserDTO;
import com.kubara.michal.quickfix.adminpanel.dto.UserEditDTO;
import com.kubara.michal.quickfix.adminpanel.exception.UserAlreadyExistsException;
import com.kubara.michal.quickfix.core.dao.RoleRepository;
import com.kubara.michal.quickfix.core.dao.StreetRepository;
import com.kubara.michal.quickfix.core.dao.UserRepository;
import com.kubara.michal.quickfix.core.entity.Address;
import com.kubara.michal.quickfix.core.entity.Estate;
import com.kubara.michal.quickfix.core.entity.Street;
import com.kubara.michal.quickfix.core.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StreetRepository streetRepository;
	
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
		
		Optional<Street> estateStreet = streetRepository.findByStreetNameAndStreetNumberAndCityAndDistrictAndPostCode(address.getStreet(), 
				address.getHouseNumber(), address.getCity(), address.getDistrict(), address.getPostCode());
		if(estateStreet.isPresent()) {
			Estate estate = estateStreet.get().getEstate();
			user.setUserEstate(estate);
		}
		
		User result = userRepository.save(user);
		
		return result;
	}
	
	private boolean userNameExistsExceptUser(String userName, User userExc) {
		User user = userRepository.findByUserName(userName);
		if(user != null) {
			if(user.getId() != userExc.getId()) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	@Transactional
	public User update(User userToEdit, UserEditDTO userDto) throws UserAlreadyExistsException {
		if(userNameExistsExceptUser(userDto.getUserName(), userToEdit)) {
			throw new UserAlreadyExistsException("Podana nazwa użytkownika jest już zajęta.", false);
		}

		userToEdit.setUserName(userDto.getUserName());
		userToEdit.setFirstName(userDto.getFirstName());
		userToEdit.setLastName(userDto.getLastName());
		userToEdit.setEmail(userDto.getEmail());
		
		Address address = userToEdit.getAddress();
		address.setCity(userDto.getCity());
		address.setDistrict(userDto.getDistrict());
		address.setPostCode(userDto.getPostCode());
		address.setPostCity(userDto.getPostCity());
		address.setStreet(userDto.getStreet());
		address.setHouseNumber(userDto.getHouseNumber());
		address.setApartmentNumber(userDto.getApartmentNumber());
		address.setPhoneNumber(userDto.getPhoneNumber());
		
		Optional<Street> estateStreet = streetRepository.findByStreetNameAndStreetNumberAndCityAndDistrictAndPostCode(address.getStreet(), 
				address.getHouseNumber(), address.getCity(), address.getDistrict(), address.getPostCode());
		if(estateStreet.isPresent()) {
			Estate estate = estateStreet.get().getEstate();
			if(userToEdit.getUserEstate() == null) {
				userToEdit.setUserEstate(estate);
			} else if(estate.getId() != userToEdit.getUserEstate().getId()) {
				userToEdit.setUserEstate(estate);
			}
		}

		User result = userRepository.save(userToEdit);

		return result;
	}

	@Override
	@Transactional
	public boolean assignUserToEstate(User user) {
		Address address = user.getAddress();
		
		Optional<Street> estateStreet = streetRepository.findByStreetNameAndStreetNumberAndCityAndDistrictAndPostCode(address.getStreet(), 
				address.getHouseNumber(), address.getCity(), address.getDistrict(), address.getPostCode());
		if(estateStreet.isPresent()) {
			Estate estate = estateStreet.get().getEstate();
			if(user.getUserEstate() == null) {
				user.setUserEstate(estate);
				return true;
			} else if(estate.getId() != user.getUserEstate().getId()) {
				user.setUserEstate(estate);
				return true;
			}
		}
		return false;
		
	}

}
