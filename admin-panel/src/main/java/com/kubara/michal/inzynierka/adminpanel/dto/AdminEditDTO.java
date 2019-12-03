package com.kubara.michal.inzynierka.adminpanel.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.kubara.michal.inzynierka.core.validation.ValidEmail;

public class AdminEditDTO {

	@NotNull(message = "Wymagane")
	private Long id;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String userName;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String firstName;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String lastName;
	
	@ValidEmail(message = "Niepoprawny email")
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String email;
	
	public AdminEditDTO() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
	
}
