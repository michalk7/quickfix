package com.kubara.michal.inzynierka.webapp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.kubara.michal.inzynierka.webapp.validation.FieldMatch;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "matchingPassword", message = "Hasła muszą być zgodne.")
})
public class SetPasswordDTO {

	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String password;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String matchingPassword;
	
	public SetPasswordDTO() {
	
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
