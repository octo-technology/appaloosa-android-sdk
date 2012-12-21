package com.octo.appaloosasdk.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadUrl {

	// used for an application binary
	@JsonProperty("download_url")
	private String downloadUrl;

	public String getDownloadUrl() {
		return downloadUrl;
	}

}
