package com.octo.appaloosasdk.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationAuthorization {
	
	private String status;

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
}
