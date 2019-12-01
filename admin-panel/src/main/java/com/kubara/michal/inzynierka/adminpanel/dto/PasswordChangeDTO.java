package com.kubara.michal.inzynierka.adminpanel.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.kubara.michal.inzynierka.core.validation.FieldMatch;
import com.kubara.michal.inzynierka.core.validation.ValidPassword;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "matchingPassword", message = "Hasła muszą być zgodne.")
})
public class PasswordChangeDTO {

	private long expertId;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	@ValidPassword(message = "Hasło nie spełnia reguł")
	private String password;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String matchingPassword;
	
	public PasswordChangeDTO() {
		
	}
	
	public PasswordChangeDTO(long expertId) {
		super();
		this.expertId = expertId;
	}

	public long getExpertId() {
		return expertId;
	}

	public void setExpertId(long expertId) {
		this.expertId = expertId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
	
}
