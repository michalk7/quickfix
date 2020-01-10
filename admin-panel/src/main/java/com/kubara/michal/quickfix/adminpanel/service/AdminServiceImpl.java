package com.kubara.michal.quickfix.adminpanel.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.quickfix.adminpanel.dao.AdminRepository;
import com.kubara.michal.quickfix.adminpanel.dao.RoleAdminPanelRepository;
import com.kubara.michal.quickfix.adminpanel.dto.AdminDTO;
import com.kubara.michal.quickfix.adminpanel.dto.AdminEditDTO;
import com.kubara.michal.quickfix.adminpanel.entity.Admin;
import com.kubara.michal.quickfix.adminpanel.entity.Role;
import com.kubara.michal.quickfix.adminpanel.exception.AdminAlreadyExistsException;

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

	@Override
	@Transactional("securityTransactionManager")
	public Optional<Admin> findById(long adminId) {
		return adminRepository.findById(adminId);
	}

	private boolean userNameExistsExceptAdmin(String userName, Admin adminExc) {
		Admin admin = adminRepository.findByUserName(userName);
		if(admin != null) {
			if(admin.getId() != adminExc.getId()) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	@Override
	@Transactional("securityTransactionManager")
	public Admin update(Admin adminToEdit, AdminEditDTO adminEditDTO) throws AdminAlreadyExistsException {
		if(userNameExistsExceptAdmin(adminEditDTO.getUserName(), adminToEdit)) {
			throw new AdminAlreadyExistsException("Podana nazwa użytkownika jest już zajęta.", false);
		}

		adminToEdit.setUserName(adminEditDTO.getUserName());
		adminToEdit.setFirstName(adminEditDTO.getFirstName());
		adminToEdit.setLastName(adminEditDTO.getLastName());
		adminToEdit.setEmail(adminEditDTO.getEmail());
		
		Admin result = adminRepository.save(adminToEdit);
		
		return result;
	}

	@Override
	@Transactional("securityTransactionManager")
	public void delete(Admin admin) {
		adminRepository.delete(admin);
	}

	@Override
	@Transactional("securityTransactionManager")
	public void changePassword(Admin admin, String password) {
		admin.setPassword(passwordEncoder.encode(password));
		adminRepository.save(admin);
	}

}
