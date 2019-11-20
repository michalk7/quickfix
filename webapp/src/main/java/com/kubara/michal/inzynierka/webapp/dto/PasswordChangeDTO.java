package com.kubara.michal.inzynierka.webapp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.kubara.michal.inzynierka.webapp.validation.FieldMatch;
import com.kubara.michal.inzynierka.webapp.validation.ValidPassword;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "matchingPassword", message = "Hasła muszą być zgodne.")
})
public class PasswordChangeDTO {

	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	@ValidPassword(message = "Hasło nie spełnia reguł")
	private String password;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String matchingPassword;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String oldPassword;
	
	public PasswordChangeDTO() {
	
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

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	
	
}
