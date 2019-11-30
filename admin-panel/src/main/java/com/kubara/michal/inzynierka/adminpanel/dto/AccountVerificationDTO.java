package com.kubara.michal.inzynierka.adminpanel.dto;

public class AccountVerificationDTO {

	private boolean verified;

	public AccountVerificationDTO() {
	
	}

	public AccountVerificationDTO(boolean verified) {
		this.verified = verified;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
}
