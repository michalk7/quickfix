package com.kubara.michal.inzynierka.webapp.util;

public class NotConfirmedCountResponse {

	private long count;
	private String error;
	
	public NotConfirmedCountResponse() {
	
	}

	public NotConfirmedCountResponse(long count) {
		this.count = count;
	}

	public NotConfirmedCountResponse(long count, String error) {
		this.count = count;
		this.error = error;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
