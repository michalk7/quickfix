package com.kubara.michal.inzynierka.webapp.dto;

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
	
	public EventDTO() {
	
	}

	public EventDTO(long id, String title, String start, String end, String problemTitle, String problemDescription, String color, String textColor, boolean confirmed) {
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

	@Override
	public String toString() {
		return "EventDTO [id=" + id + ", title=" + title + ", start=" + start + ", end=" + end + ", problemTitle="
				+ problemTitle + ", problemDescription=" + problemDescription + ", color=" + color + ", textColor="
				+ textColor + "]";
	}

	
	
}
