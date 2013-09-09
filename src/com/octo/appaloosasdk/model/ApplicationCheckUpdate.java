package com.octo.appaloosasdk.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationCheckUpdate {
	
	public static enum Status {
		UPDATE_NEEDED, 
		NO_UPDATE_NEEDED
	}

	private String status;
	private int id;
	@JsonProperty("download_url")
	private String downloadUrl;
	
	public boolean isUpdateNeeded() {
		if(getStatus() != null && status.equals(Status.UPDATE_NEEDED.toString())) {
			return true;
		}
		return false;
	}
	
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
	public String getDownloadUrl() { return downloadUrl; }
	public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
}
