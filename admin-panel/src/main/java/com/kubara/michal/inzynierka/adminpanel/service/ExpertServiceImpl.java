package com.kubara.michal.inzynierka.adminpanel.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.adminpanel.dto.ExpertDTO;
import com.kubara.michal.inzynierka.adminpanel.dto.ExpertEditDTO;
import com.kubara.michal.inzynierka.adminpanel.exception.UserAlreadyExistsException;
import com.kubara.michal.inzynierka.core.dao.RoleRepository;
import com.kubara.michal.inzynierka.core.dao.StreetRepository;
import com.kubara.michal.inzynierka.core.dao.UserRepository;
import com.kubara.michal.inzynierka.core.entity.Address;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.Street;
import com.kubara.michal.inzynierka.core.entity.User;

@Service
public class ExpertServiceImpl implements ExpertService {
	
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
		return userRepository.findAllByRoles(roleRepository.findByName("ROLE_EXPERT"), pageable);
	}

	@Override
	@Transactional
	public Optional<User> findById(long expertId) {
		return userRepository.findById(expertId);
	}

	@Override
	@Transactional
	public boolean isExpert(User expert) {
		return expert.getRoles().contains(roleRepository.findByName("ROLE_EXPERT"));
	}

	@Override
	@Transactional
	public void delete(User expert) {
		userRepository.delete(expert);
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
	public User saveExpert(ExpertDTO expertDto) throws UserAlreadyExistsException {
		if(emailExists(expertDto.getEmail())) {
			throw new UserAlreadyExistsException("Podany adres email jest już zajęty.", true);
		}
		
		if(userNameExists(expertDto.getUserName())) {
			throw new UserAlreadyExistsException("Podana nazwa użytkownika jest już zajęta.", false);
		}
		
		User user = new User();
		user.setUserName(expertDto.getUserName());
		user.setPassword(passwordEncoder.encode(expertDto.getPassword()));
		user.setFirstName(expertDto.getFirstName());
		user.setLastName(expertDto.getLastName());
		user.setEmail(expertDto.getEmail());
		
		Address address = new Address();
		address.setCity(expertDto.getCity());
		address.setDistrict(expertDto.getDistrict());
		address.setPostCode(expertDto.getPostCode());
		address.setPostCity(expertDto.getPostCity());
		address.setStreet(expertDto.getStreet());
		address.setHouseNumber(expertDto.getHouseNumber());
		address.setApartmentNumber(expertDto.getApartmentNumber());
		address.setPhoneNumber(expertDto.getPhoneNumber());
		
		address.setUser(user);
		user.setAddress(address);
		
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_EXPERT")));
		
		user.setCategories(expertDto.getSelectedCategoriesFromCheckboxes());
		
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
	public User update(User expertToEdit, ExpertEditDTO expertDto) throws UserAlreadyExistsException {
		
		if(userNameExistsExceptUser(expertDto.getUserName(), expertToEdit)) {
			throw new UserAlreadyExistsException("Podana nazwa użytkownika jest już zajęta.", false);
		}

		expertToEdit.setUserName(expertDto.getUserName());
		expertToEdit.setFirstName(expertDto.getFirstName());
		expertToEdit.setLastName(expertDto.getLastName());
		expertToEdit.setEmail(expertDto.getEmail());
		expertToEdit.setCategories(expertDto.getSelectedCategoriesFromCheckboxes());
		
		Address address = expertToEdit.getAddress();
		address.setCity(expertDto.getCity());
		address.setDistrict(expertDto.getDistrict());
		address.setPostCode(expertDto.getPostCode());
		address.setPostCity(expertDto.getPostCity());
		address.setStreet(expertDto.getStreet());
		address.setHouseNumber(expertDto.getHouseNumber());
		address.setApartmentNumber(expertDto.getApartmentNumber());
		address.setPhoneNumber(expertDto.getPhoneNumber());
		
		Optional<Street> estateStreet = streetRepository.findByStreetNameAndStreetNumberAndCityAndDistrictAndPostCode(address.getStreet(), 
				address.getHouseNumber(), address.getCity(), address.getDistrict(), address.getPostCode());
		if(estateStreet.isPresent()) {
			Estate estate = estateStreet.get().getEstate();
			if(estate.getId() != expertToEdit.getUserEstate().getId()) {
				expertToEdit.setUserEstate(estate);
			}
		}

		User result = userRepository.save(expertToEdit);

		return result;
		
	}
	
	

}
