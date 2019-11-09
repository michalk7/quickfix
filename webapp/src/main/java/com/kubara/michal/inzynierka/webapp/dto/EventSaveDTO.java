package com.kubara.michal.inzynierka.webapp.dto;

public class EventSaveDTO {
	
	private String problemTitle;
	private String problemDescription;
	private String startDate;
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
