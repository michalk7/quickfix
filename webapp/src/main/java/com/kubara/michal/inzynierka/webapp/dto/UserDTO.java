package com.kubara.michal.inzynierka.webapp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.kubara.michal.inzynierka.webapp.validation.FieldMatch;
import com.kubara.michal.inzynierka.webapp.validation.ValidEmail;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "matchingPassword", message = "Hasła muszą być zgodne.")
})
public class UserDTO {

	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String userName;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
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
	
	@ValidEmail
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String email;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String city;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String postCode;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String postCity;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String street;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String houseNumber;
	
	
	private String apartmentNumber;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String phoneNumber;
	
	public UserDTO() {
	
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

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPostCity() {
		return postCity;
	}

	public void setPostCity(String postCity) {
		this.postCity = postCity;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	
	
}
