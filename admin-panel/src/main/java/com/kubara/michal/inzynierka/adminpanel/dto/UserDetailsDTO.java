package com.kubara.michal.inzynierka.adminpanel.dto;

public class UserDetailsDTO {

	private String city;
	private String district;
	private String postCode;
	private String postCity;
	private String street;
	private String houseNumber;
	private String apartmentNumber;
	private String phoneNumber;
	
	public UserDetailsDTO() {
	
	}

	public UserDetailsDTO(String city, String district, String postCode, String postCity, String street,
			String houseNumber, String apartmentNumber, String phoneNumber) {
		this.city = city;
		this.district = district;
		this.postCode = postCode;
		this.postCity = postCity;
		this.street = street;
		this.houseNumber = houseNumber;
		this.apartmentNumber = apartmentNumber;
		this.phoneNumber = phoneNumber;
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
	
}
