package com.kubara.michal.inzynierka.adminpanel.dto;

import javax.validation.constraints.NotNull;

public class EstateChoiceDTO {

	@NotNull(message = "Wymagane")
	private long expertId;
	
	@NotNull(message = "Wymagane")
	private Long estateId;
	
	public EstateChoiceDTO() {
	
	}

	public EstateChoiceDTO(long expertId) {
		super();
		this.expertId = expertId;
	}



	public long getExpertId() {
		return expertId;
	}

	public void setExpertId(long expertId) {
		this.expertId = expertId;
	}

	public Long getEstateId() {
		return estateId;
	}

	public void setEstateId(Long estateId) {
		this.estateId = estateId;
	}
	
}
