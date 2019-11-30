package com.kubara.michal.inzynierka.adminpanel.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.kubara.michal.inzynierka.core.entity.Category;
import com.kubara.michal.inzynierka.core.validation.ValidEmail;
import com.kubara.michal.inzynierka.core.validation.ValidPolishPhoneNumber;

public class ExpertEditDTO {
	
	private long id;

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
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String city;
	
	private String district;
	
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
	@ValidPolishPhoneNumber
	private String phoneNumber;
	
	private List<Category> selectedCategoriesFromCheckboxes = new ArrayList<>();;
	
	public ExpertEditDTO() {
	
	}
	
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
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
	
	public List<Category> getSelectedCategoriesFromCheckboxes() {
		return selectedCategoriesFromCheckboxes;
	}

	public void setSelectedCategoriesFromCheckboxes(List<Category> selectedCategoriesFromCheckboxes) {
		this.selectedCategoriesFromCheckboxes = selectedCategoriesFromCheckboxes;
	}
	
}
