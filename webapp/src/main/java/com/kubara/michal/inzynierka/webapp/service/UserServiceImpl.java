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
import com.kubara.michal.inzynierka.core.dao.VerificationTokenRepository;
import com.kubara.michal.inzynierka.core.entity.Address;
import com.kubara.michal.inzynierka.core.entity.Role;
import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.core.entity.VerificationToken;
import com.kubara.michal.inzynierka.webapp.dto.UserDTO;
import com.kubara.michal.inzynierka.webapp.validation.UserAlreadyExistsException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
    private VerificationTokenRepository tokenRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
	    boolean accountNonLocked = true;
		
		User user = userRepository.findByUserName(username);
		if(user == null) {
			throw new UsernameNotFoundException("Niepoprawna nazwa użytkownika lub hasło.");
		}
		
		return new org.springframework.security.core.userdetails.User(
				user.getUserName(), 
				user.getPassword(), 
				user.isEnabled(), 
				accountNonExpired, 
		        credentialsNonExpired, 
		        accountNonLocked, 
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
	public User save(UserDTO userDTO, String roleName) throws UserAlreadyExistsException {
		
		if(emailExists(userDTO.getEmail())) {
			throw new UserAlreadyExistsException("Podany adres email jest już zajęty.", true);
		}
		
		if(userNameExists(userDTO.getUserName())) {
			throw new UserAlreadyExistsException("Podana nazwa użytkowanika jest już zajęta.", false);
		}
		
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
		
		User result = userRepository.save(user);

		return result;
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
	public void saveOrUpdateRegisteredUser(User user) {
		userRepository.save(user);
	}

	@Override
	public User getUser(String verificationToken) {
		return tokenRepository.findByToken(verificationToken).getUser();
	}

	@Override
	public void createVerificationToken(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
	}

	@Override
	public VerificationToken getVerificationToken(String VerificationToken) {
		return tokenRepository.findByToken(VerificationToken);
	}

}