package org.rebit.auth.model;

import org.springframework.http.HttpStatus;

public class StatusAndMessage {
	private HttpStatus httpStatus;
	private String message;
	public StatusAndMessage(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
	
