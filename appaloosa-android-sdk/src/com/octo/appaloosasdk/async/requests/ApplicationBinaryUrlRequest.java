package com.octo.appaloosasdk.async.requests;

import com.octo.android.robospice.request.SpiceRequest;
import com.octo.appaloosasdk.model.DownloadUrl;
import com.octo.appaloosasdk.webservices.WebServices;

/**
 * {@link SpiceRequest} used to retrieve the application binary download url ({@link DownloadUrl}), this url will then be used to download the APK
 * 
 * @author Jerome Van Der Linden
 * 
 */
public class ApplicationBinaryUrlRequest extends SpiceRequest<DownloadUrl> {

	private long storeId;
	private long applicationId;
	private String storeToken;

	public ApplicationBinaryUrlRequest(long storeId, long applicationId, String storeToken) {
		super(DownloadUrl.class);
		this.storeId = storeId;
		this.applicationId = applicationId;
		this.storeToken = storeToken;
	}

	@Override
	public DownloadUrl loadDataFromNetwork() throws Exception {
		return WebServices.getInstance().getApplicationDownloadUrl(storeId, applicationId, storeToken);
	}

}
