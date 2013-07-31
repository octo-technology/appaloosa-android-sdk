package com.octo.appaloosasdk.async.requests;

import com.octo.android.robospice.request.SpiceRequest;
import com.octo.appaloosasdk.model.Application;
import com.octo.appaloosasdk.model.ApplicationAuthorization;
import com.octo.appaloosasdk.webservices.WebServices;

/**
 * {@link SpiceRequest} used to retrieve if the device can launch the application ({@link Application})
 * 
 * @author Christopher Parola
 * 
 */
public class ApplicationAuthorizationRequest extends SpiceRequest<ApplicationAuthorization> {

	private String packageName;
	private long storeId;
	private String storeToken;
	private String imei;
	private int versionCode;
	private String locale;

	public ApplicationAuthorizationRequest(String packageName, int versionCode, long storeId, String storeToken, String imei, String locale) {
		super(ApplicationAuthorization.class);
		this.packageName = packageName;
		this.storeId = storeId;
		this.storeToken = storeToken;
		this.imei = imei;
		this.versionCode = versionCode;
		this.locale = locale;
	}

	@Override
	public ApplicationAuthorization loadDataFromNetwork() throws Exception {
		return WebServices.getInstance().getApplicationAuthorizations(packageName, versionCode, storeId, storeToken, imei, locale);
	}
}
