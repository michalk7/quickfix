package com.kubara.michal.inzynierka.adminpanel.dto;

public class AccountStatusDTO {

	private boolean enabled;
	
	public AccountStatusDTO() {

	}

	public AccountStatusDTO(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	
	
	
}
