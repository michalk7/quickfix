package com.kubara.michal.inzynierka.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kubara.michal.inzynierka.core.entity.User;
import com.kubara.michal.inzynierka.core.entity.VerificationToken;
import com.kubara.michal.inzynierka.webapp.dto.ExpertDTO;
import com.kubara.michal.inzynierka.webapp.dto.UserDTO;
import com.kubara.michal.inzynierka.webapp.validation.UserAlreadyExistsException;

public interface UserService extends UserDetailsService {
	
	public User findByUserName(String userName);
	
	User getUser(String verificationToken);

	public User save(UserDTO userDTO, String roleName) throws UserAlreadyExistsException;
	
	void saveOrUpdateRegisteredUser(User user);
	
	void createVerificationToken(User user, String token);
	 
    VerificationToken getVerificationToken(String VerificationToken);

    void delete(User user);
    
    void deleteById(long id);
    
    public User findByEmail(String email);

	public VerificationToken generateNewVerificationToken(String existingToken);

	public User saveExpert(ExpertDTO dtoExpert, String roleName) throws UserAlreadyExistsException;

	Optional<User> findById(long userId);
	
	List<User> findAll();
    
}
