package com.kubara.michal.quickfix.webapp.dto;

public class EventConfirmationDTO {
	
	private boolean confirmed;
	
	public EventConfirmationDTO() {
	
	}

	public EventConfirmationDTO(boolean confirmed) {
		super();
		this.confirmed = confirmed;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	
	

}
