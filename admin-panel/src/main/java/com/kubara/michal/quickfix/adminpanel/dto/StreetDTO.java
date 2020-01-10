package com.kubara.michal.quickfix.adminpanel.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StreetDTO {
	
	@NotNull(message = "Wymagane")
	private Long estateId;
	
	private long id;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String streetName;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String streetNumber;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String city;
	
	private String district;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String postCode;
	
	public StreetDTO() {

	}

	public Long getEstateId() {
		return estateId;
	}

	public void setEstateId(Long estateId) {
		this.estateId = estateId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
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
	
	

}
