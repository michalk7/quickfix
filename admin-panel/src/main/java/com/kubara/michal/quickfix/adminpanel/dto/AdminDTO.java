package com.kubara.michal.quickfix.adminpanel.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.kubara.michal.quickfix.core.validation.FieldMatch;
import com.kubara.michal.quickfix.core.validation.ValidEmail;
import com.kubara.michal.quickfix.core.validation.ValidPassword;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "matchingPassword", message = "Hasła muszą być zgodne.")
})
public class AdminDTO {

	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String userName;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	@ValidPassword(message = "Hasło nie spełnia reguł")
	private String password;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String matchingPassword;
	
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
	
	public AdminDTO() {
	
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

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
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
