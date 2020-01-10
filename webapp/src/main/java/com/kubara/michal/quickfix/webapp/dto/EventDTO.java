package com.kubara.michal.quickfix.webapp.dto;

public class EventDTO {

	private long id;
	private String title;
	private String start;
	private String end;
	private String problemTitle;
	private String problemDescription;
	private String color;
	private String textColor;
	private boolean confirmed;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	
	public EventDTO() {
	
	}

	public EventDTO(long id, String title, String start, String end, String problemTitle, String problemDescription,
			String color, String textColor, boolean confirmed, String firstName, String lastName, String phoneNumber) {
		super();
		this.id = id;
		this.title = title;
		this.start = start;
		this.end = end;
		this.problemTitle = problemTitle;
		this.problemDescription = problemDescription;
		this.color = color;
		this.textColor = textColor;
		this.confirmed = confirmed;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String startDate) {
		this.start = startDate;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String endDate) {
		this.end = endDate;
	}

	public String getProblemTitle() {
		return problemTitle;
	}

	public void setProblemTitle(String problemTitle) {
		this.problemTitle = problemTitle;
	}

	public String getProblemDescription() {
		return problemDescription;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
