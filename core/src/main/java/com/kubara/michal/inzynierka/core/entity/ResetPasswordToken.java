package com.kubara.michal.inzynierka.core.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ResetPasswordToken {

	private static final int EXPIRATION = 60 * 24;
	
	private long id;
	private String token;
	
	private User user;
	
	private Date expirationDate;
	
	public ResetPasswordToken() {
	
	}

	public ResetPasswordToken(String token) {
		super();
		this.token = token;
		this.expirationDate = calculateExpirationDate(EXPIRATION);
	}
	
	
	public ResetPasswordToken(String token, User user) {
		super();
		this.token = token;
		this.user = user;
		this.expirationDate = calculateExpirationDate(EXPIRATION);
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	private Date calculateExpirationDate(int expiryTimeInMinutes) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}
	
	public void updateToken(final String token) {
        this.token = token;
        this.expirationDate = calculateExpirationDate(EXPIRATION);
    }
	
	
}
