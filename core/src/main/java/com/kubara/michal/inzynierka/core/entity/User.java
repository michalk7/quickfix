package com.kubara.michal.inzynierka.core.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	private boolean verified;
	
	private Collection<Role> roles;
	private Address address;
	private VerificationToken verificationToken;
	private ResetPasswordToken resetPasswordToken;
	private Collection<Category> categories;
	private Estate userEstate;
	private Collection<Estate> expertEstates;
	private Collection<Event> userEvents;
	private Collection<Event> expertEvents;
	
	public User() {
	
	}
	
	public User(String userName, String password, String firstName, String lastName, String email) {
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		enabled = false;
		verified = false;
	}

	public User(String userName, String password, String firstName, String lastName, String email,
			Collection<Role> roles) {
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		enabled = false;
		verified = false;
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

	@Column(nullable = false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(nullable = false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(nullable = false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(nullable = false)
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

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	public ResetPasswordToken getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(ResetPasswordToken resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	@ManyToMany(fetch=FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name="users_categories",
		joinColumns = @JoinColumn(name="user_id"),
		inverseJoinColumns = @JoinColumn(name="category_id"),
		uniqueConstraints = @UniqueConstraint(columnNames = { "category_id", "user_id" })
	)
	public Collection<Category> getCategories() {
		return categories;
	}

	public void setCategories(Collection<Category> categories) {
		this.categories = categories;
	}

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH } )
	@JoinColumn(name="user_estate_id")
	public Estate getUserEstate() {
		return userEstate;
	}

	public void setUserEstate(Estate userEstate) {
		this.userEstate = userEstate;
	}

	@ManyToMany(fetch=FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name="experts_estates",
		joinColumns = @JoinColumn(name="expert_id"),
		inverseJoinColumns = @JoinColumn(name="estate_id"),
		uniqueConstraints = @UniqueConstraint(columnNames = { "estate_id", "expert_id" })
	)
	public Collection<Estate> getExpertEstates() {
		return expertEstates;
	}

	public void setExpertEstates(Collection<Estate> expertEstates) {
		this.expertEstates = expertEstates;
	}

	@OneToMany(mappedBy = "user")
	public Collection<Event> getUserEvents() {
		return userEvents;
	}

	public void setUserEvents(Collection<Event> userEvents) {
		this.userEvents = userEvents;
	}

	@OneToMany(mappedBy = "expert")
	public Collection<Event> getExpertEvents() {
		return expertEvents;
	}

	public void setExpertEvents(Collection<Event> expertEvents) {
		this.expertEvents = expertEvents;
	}
	
	
	
	
}
