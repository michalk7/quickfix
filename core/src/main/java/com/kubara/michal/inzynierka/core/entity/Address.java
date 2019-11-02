package com.kubara.michal.inzynierka.core.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Address {

	private long id;
	private String city;
	private String district;
	private String postCode;
	private String postCity;
	private String street;
	private String houseNumber;
	private String apartmentNumber;
	private String phoneNumber;
	
	private User user;
	
	public Address() {
	
	}

	public Address(String city, String district, String postCode, String postCity, String street, String houseNumber,
			String apartmentNumber, String phoneNumber) {
		this.city = city;
		this.district = district;
		this.postCode = postCode;
		this.postCity = postCity;
		this.street = street;
		this.houseNumber = houseNumber;
		this.apartmentNumber = apartmentNumber;
		this.phoneNumber = phoneNumber;
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
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(nullable = true)
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(nullable = false)
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(nullable = false)
	public String getPostCity() {
		return postCity;
	}

	public void setPostCity(String postCity) {
		this.postCity = postCity;
	}

	@Column(nullable = false)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(nullable = false)
	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	@Column(nullable = true)
	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	@Column(nullable = false)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@OneToOne(mappedBy = "address", cascade={ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
}
