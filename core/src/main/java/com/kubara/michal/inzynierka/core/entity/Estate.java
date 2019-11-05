package com.kubara.michal.inzynierka.core.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Estate {

	private long id;
	private String name;
	private Set<Street> streets;
	private Set<User> users;
	private Set<User> experts;
	
	
	public Estate() {
	
	}


	public Estate(String name) {
		this.name = name;
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	@Column(nullable = false)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@OneToMany(mappedBy="estate")
	public Set<Street> getStreets() {
		return streets;
	}


	public void setStreets(Set<Street> streets) {
		this.streets = streets;
	}

	@OneToMany(mappedBy = "userEstate")
	public Set<User> getUsers() {
		return users;
	}


	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@ManyToMany(mappedBy = "expertEstates")
	public Set<User> getExperts() {
		return experts;
	}


	public void setExperts(Set<User> experts) {
		this.experts = experts;
	}
	
	
	
}
