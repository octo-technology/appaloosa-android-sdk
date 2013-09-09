package com.octo.appaloosasdk.async.requests;

import com.octo.android.robospice.request.SpiceRequest;
import com.octo.appaloosasdk.model.Application;
import com.octo.appaloosasdk.model.ApplicationCheckUpdate;
import com.octo.appaloosasdk.webservices.WebServices;

/**
 * {@link SpiceRequest} used to retrieve information about the application ({@link Application}) on Appaloosa server
 * 
 * @author Jerome Van Der Linden
 * 
 */
public class ApplicationCheckForUpdateRequest extends SpiceRequest<ApplicationCheckUpdate> {

	private String packageName;
	private long storeId;
	private String storeToken;
	private int versionCode;
	private String encryptedDeviceId;
	
	public ApplicationCheckForUpdateRequest(String packageName, int versionCode, long storeId, String storeToken, String encryptedDeviceId) {
		super(ApplicationCheckUpdate.class);
		this.packageName = packageName;
		this.storeId = storeId;
		this.storeToken = storeToken;
		this.versionCode = versionCode;
		this.encryptedDeviceId = encryptedDeviceId;
	}

	@Override
	public ApplicationCheckUpdate loadDataFromNetwork() throws Exception {
		return WebServices.getInstance().getApplicationCheckForUpdate(packageName, storeId, storeToken, encryptedDeviceId, versionCode);
	}
}
