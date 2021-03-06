package com.kubara.michal.quickfix.webapp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EventSaveDTO {
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String problemTitle;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String problemDescription;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String startDate;
	
	@NotNull(message = "Wymagane")
	@Size(min = 1, message = "Wymagane")
	private String endDate;

	public EventSaveDTO() {

	}

	public EventSaveDTO(String problemTitle, String problemDescription, String startDate, String endDate) {
		super();
		this.problemTitle = problemTitle;
		this.problemDescription = problemDescription;
		this.startDate = startDate;
		this.endDate = endDate;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
	
}
