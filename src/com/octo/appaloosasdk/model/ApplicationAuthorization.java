package com.octo.appaloosasdk.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationAuthorization {
	
	public static enum Status {
		UNKNOWN_DEVICE, UNREGISTERED_DEVICE, AUTHORIZED, NOT_AUTHORIZED, UNKNOWN, NO_NETWORK, REQUEST_ERROR
	}

	private String status;
	private String message;
	
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
}
