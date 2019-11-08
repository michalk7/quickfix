package com.kubara.michal.inzynierka.webapp.dto;

public class EventDTO {

	private long id;
	private String title;
	private String start;
	private String end;
	private String problemTitle;
	private String problemDescription;
	
	public EventDTO() {
	
	}

	public EventDTO(long id, String title, String start, String end, String problemTitle, String problemDescription) {
		super();
		this.id = id;
		this.title = title;
		this.start = start;
		this.end = end;
		this.problemTitle = problemTitle;
		this.problemDescription = problemDescription;
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

	@Override
	public String toString() {
		return "EventDTO [id=" + id  + ", title=" + title + ", startDate=" + start + ", endDate=" + end + "]";
	}
	
	
	
}
