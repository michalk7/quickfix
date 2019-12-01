package com.kubara.michal.inzynierka.adminpanel.dto;

import java.util.List;

public class ExpertDetailsDTO {

	private String city;
	private String district;
	private String postCode;
	private String postCity;
	private String street;
	private String houseNumber;
	private String apartmentNumber;
	private String phoneNumber;
	private List<String> categories;
	private List<String> expertEstates;
	
	public ExpertDetailsDTO() {

	}

	public ExpertDetailsDTO(String city, String district, String postCode, String postCity, String street, String houseNumber,
			String apartmentNumber, String phoneNumber, List<String> categories, List<String> expertEstates) {
		this.city = city;
		this.district = district;
		this.postCode = postCode;
		this.postCity = postCity;
		this.street = street;
		this.houseNumber = houseNumber;
		this.apartmentNumber = apartmentNumber;
		this.phoneNumber = phoneNumber;
		this.categories = categories;
		this.expertEstates = expertEstates;
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

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getExpertEstates() {
		return expertEstates;
	}

	public void setExpertEstates(List<String> expertEstates) {
		this.expertEstates = expertEstates;
	}
	
	
	
}
