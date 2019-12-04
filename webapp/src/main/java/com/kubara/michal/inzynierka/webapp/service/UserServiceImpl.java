package com.kubara.michal.inzynierka.webapp.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.inzynierka.core.dao.ResetPasswordTokenRepository;
import com.kubara.michal.inzynierka.core.dao.RoleRepository;
import com.kubara.michal.inzynierka.core.dao.StreetRepository;
import com.kubara.michal.inzynierka.core.dao.UserRepository;
import com.kubara.michal.inzynierka.core.dao.VerificationTokenRepository;
import com.kubara.michal.inzynierka.core.entity.Address;
import com.kubara.michal.inzynierka.core.entity.Estate;
import com.kubara.michal.inzynierka.core.entity.ResetPasswordToken;
import com.kubara.michal.inzynierka.core.entity.Role;
import com.kubara.michal.inzynierka.core.entity.Street;
import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.core.entity.VerificationToken;
import com.kubara.michal.inzynierka.webapp.dto.ExpertDTO;
import com.kubara.michal.inzynierka.webapp.dto.PasswordChangeDTO;
import com.kubara.michal.inzynierka.webapp.dto.UserDTO;
import com.kubara.michal.inzynierka.webapp.dto.UserEditDTO;
import com.kubara.michal.inzynierka.webapp.exception.UserAlreadyExistsException;
import com.kubara.michal.inzynierka.webapp.exception.WrongPasswordException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
    private VerificationTokenRepository tokenRepository;
	
	@Autowired
	private ResetPasswordTokenRepository resetPasswordTokenRepository;
	
	@Autowired
	private StreetRepository streetRepository;
	
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
		
		boolean enabled = user.isEnabled();
		if(enabled && user.getRoles().stream().anyMatch(e -> e.getName().equals("ROLE_EXPERT"))) {
			if(!user.isVerified()) {
				//ustawiamy enabled, żeby pod kodem błędu dla credentials expired dać komunikat o braku weryfikacji konta
				enabled = true;
				credentialsNonExpired = false;
			}
		}
		
		return new org.springframework.security.core.userdetails.User(
				user.getUserName(), 
				user.getPassword(), 
				enabled, 
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
			throw new UserAlreadyExistsException("Podana nazwa użytkownika jest już zajęta.", false);
		}
		
		User user = new User();
		user.setUserName(userDTO.getUserName());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		
		Address address = new Address();
		address.setCity(userDTO.getCity());
		address.setDistrict(userDTO.getDistrict());
		address.setPostCode(userDTO.getPostCode());
		address.setPostCity(userDTO.getPostCity());
		address.setStreet(userDTO.getStreet());
		address.setHouseNumber(userDTO.getHouseNumber());
		address.setApartmentNumber(userDTO.getApartmentNumber());
		address.setPhoneNumber(userDTO.getPhoneNumber());
		
		address.setUser(user);
		user.setAddress(address);
		
		user.setRoles(Arrays.asList(roleRepository.findByName(roleName)));
		
		Optional<Street> estateStreet = streetRepository.findByStreetNameAndStreetNumberAndCityAndDistrictAndPostCode(address.getStreet(), 
											address.getHouseNumber(), address.getCity(), address.getDistrict(), address.getPostCode());
		if(estateStreet.isPresent()) {
			Estate estate = estateStreet.get().getEstate();
			user.setUserEstate(estate);
		}
		
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
	@Transactional
	public User getUser(String verificationToken) {
		return tokenRepository.findByToken(verificationToken).getUser();
	}

	@Override
	@Transactional
	public void createVerificationToken(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
	}

	@Override
	@Transactional
	public VerificationToken getVerificationToken(String VerificationToken) {
		return tokenRepository.findByToken(VerificationToken);
	}

	@Override
	@Transactional
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	@Transactional
	public void deleteById(long id) {
		userRepository.deleteById(id);
	}

	@Override
	@Transactional
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public VerificationToken generateNewVerificationToken(String existingToken) {
		String newToken = UUID.randomUUID().toString();
		VerificationToken token = tokenRepository.findByToken(existingToken);
		token.updateToken(newToken);
		tokenRepository.save(token);
		return token;
	}

	@Override
	@Transactional
	public User saveExpert(ExpertDTO dtoExpert, String roleName) throws UserAlreadyExistsException {
		if(emailExists(dtoExpert.getEmail())) {
			throw new UserAlreadyExistsException("Podany adres email jest już zajęty.", true);
		}
		
		if(userNameExists(dtoExpert.getUserName())) {
			throw new UserAlreadyExistsException("Podana nazwa użytkownika jest już zajęta.", false);
		}
		
		User user = new User();
		user.setUserName(dtoExpert.getUserName());
		user.setPassword(passwordEncoder.encode(dtoExpert.getPassword()));
		user.setFirstName(dtoExpert.getFirstName());
		user.setLastName(dtoExpert.getLastName());
		user.setEmail(dtoExpert.getEmail());
		
		Address address = new Address();
		address.setCity(dtoExpert.getCity());
		address.setDistrict(dtoExpert.getDistrict());
		address.setPostCode(dtoExpert.getPostCode());
		address.setPostCity(dtoExpert.getPostCity());
		address.setStreet(dtoExpert.getStreet());
		address.setHouseNumber(dtoExpert.getHouseNumber());
		address.setApartmentNumber(dtoExpert.getApartmentNumber());
		address.setPhoneNumber(dtoExpert.getPhoneNumber());
		
		address.setUser(user);
		user.setAddress(address);
		
		user.setRoles(Arrays.asList(roleRepository.findByName(roleName)));
		
		user.setCategories(dtoExpert.getSelectedCategoriesFromCheckboxes());
		
		User result = userRepository.save(user);

		return result;
	}

	@Override
	@Transactional
	public Optional<User> findById(long userId) {
		return userRepository.findById(userId);
	}

	@Override
	@Transactional
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	@Transactional
	public User update(User userToEdit, UserEditDTO dtoUser) throws UserAlreadyExistsException {
		
		if(userNameExistsExceptUser(dtoUser.getUserName(), userToEdit)) {
			throw new UserAlreadyExistsException("Podana nazwa użytkownika jest już zajęta.", false);
		}
		
		
		userToEdit.setUserName(dtoUser.getUserName());
		userToEdit.setFirstName(dtoUser.getFirstName());
		userToEdit.setLastName(dtoUser.getLastName());
		
		Address address = userToEdit.getAddress();
		address.setCity(dtoUser.getCity());
		address.setDistrict(dtoUser.getDistrict());
		address.setPostCode(dtoUser.getPostCode());
		address.setPostCity(dtoUser.getPostCity());
		address.setStreet(dtoUser.getStreet());
		address.setHouseNumber(dtoUser.getHouseNumber());
		address.setApartmentNumber(dtoUser.getApartmentNumber());
		address.setPhoneNumber(dtoUser.getPhoneNumber());
		
		if(userToEdit.getRoles().stream().anyMatch(e -> e.getName().equals("ROLE_USER"))) {
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

		}
		
		User result = userRepository.save(userToEdit);

		return result;
	}
	
	@Override
	@Transactional
	public void changePassword(User user, PasswordChangeDTO passwordDTO) throws WrongPasswordException {
		if(!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
			throw new WrongPasswordException("Błędne hasło.");
		}
		user.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
		userRepository.save(user);
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
	public void createResetPasswordToken(User user, String token) {
		ResetPasswordToken oldToken = resetPasswordTokenRepository.findByUser(user);
		if(oldToken != null) {
			user.setResetPasswordToken(null);
			resetPasswordTokenRepository.delete(oldToken);
		}
		ResetPasswordToken newToken = new ResetPasswordToken(token, user);
		resetPasswordTokenRepository.save(newToken);		
	}

	@Override
	@Transactional
	public void setNewPassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void deleteResetToken(ResetPasswordToken resetPasswordToken) {
		resetPasswordToken.getUser().setResetPasswordToken(null);
		resetPasswordTokenRepository.delete(resetPasswordToken);
	}

	

}
