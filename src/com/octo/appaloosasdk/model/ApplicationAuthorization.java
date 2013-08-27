package com.octo.appaloosasdk.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationAuthorization {
	
	public static enum Status {
		UNKNOWN_APPLICATION, AUTHORIZED, UNREGISTERED_DEVICE, UNKNOWN_DEVICE, NOT_AUTHORIZED, DEVICE_ID_FORMAT_ERROR, NO_NETWORK, REQUEST_ERROR, UNKNOWN
	}

	private String status;
	private String message;
	
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
}
