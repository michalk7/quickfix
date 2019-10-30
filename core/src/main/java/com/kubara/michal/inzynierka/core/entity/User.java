package com.kubara.michal.inzynierka.core.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;

@Entity
public class User {

	private long id;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private boolean enabled;
	
	private Collection<Role> roles;
	private Address address;
	private VerificationToken verificationToken;
	
	public User() {
	
	}
	
	public User(String userName, String password, String firstName, String lastName, String email) {
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		enabled = false;
	}

	public User(String userName, String password, String firstName, String lastName, String email,
			Collection<Role> roles) {
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		enabled = false;
		this.roles = roles;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@ManyToMany(fetch=FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name="users_roles",
		joinColumns = @JoinColumn(name="user_id"),
		inverseJoinColumns = @JoinColumn(name="role_id"),
		uniqueConstraints = @UniqueConstraint(columnNames = { "role_id", "user_id" })
	)
	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	public VerificationToken getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(VerificationToken verificationToken) {
		this.verificationToken = verificationToken;
	}
	
	
	
	
	
}
