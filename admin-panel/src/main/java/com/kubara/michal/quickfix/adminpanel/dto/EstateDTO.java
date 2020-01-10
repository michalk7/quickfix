package com.kubara.michal.quickfix.adminpanel.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EstateDTO {

	private long id;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String name;
	
	public EstateDTO() {
	
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
