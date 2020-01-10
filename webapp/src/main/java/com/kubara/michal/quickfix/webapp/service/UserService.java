package com.kubara.michal.quickfix.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kubara.michal.quickfix.core.entity.ResetPasswordToken;
import com.kubara.michal.quickfix.core.entity.User;
import com.kubara.michal.quickfix.core.entity.VerificationToken;
import com.kubara.michal.quickfix.webapp.dto.ExpertDTO;
import com.kubara.michal.quickfix.webapp.dto.PasswordChangeDTO;
import com.kubara.michal.quickfix.webapp.dto.UserDTO;
import com.kubara.michal.quickfix.webapp.dto.UserEditDTO;
import com.kubara.michal.quickfix.webapp.exception.UserAlreadyExistsException;
import com.kubara.michal.quickfix.webapp.exception.WrongPasswordException;

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

	User update(User userToEdit, UserEditDTO dtoUser) throws UserAlreadyExistsException;
	
	void changePassword(User user, PasswordChangeDTO passwordDTO) throws WrongPasswordException;

	void createResetPasswordToken(User user, String token);

	void setNewPassword(User user, String password);

	void deleteResetToken(ResetPasswordToken resetPasswordToken);
    
}
