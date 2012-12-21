package com.octo.appaloosasdk.async.requests;

import com.octo.android.robospice.request.SpiceRequest;
import com.octo.appaloosasdk.model.Application;
import com.octo.appaloosasdk.webservices.WebServices;

/**
 * {@link SpiceRequest} used to retrieve information about the application ({@link Application}) on Appaloosa server
 * 
 * @author Jerome Van Der Linden
 * 
 */
public class ApplicationInformationRequest extends SpiceRequest<Application> {

	private String mPackageName;
	private long mStoreId;
	private String mStoreToken;

	public ApplicationInformationRequest(String packageName, long storeId, String storeToken) {
		super(Application.class);
		mPackageName = packageName;
		mStoreId = storeId;
		mStoreToken = storeToken;
	}

	@Override
	public Application loadDataFromNetwork() throws Exception {
		return WebServices.getInstance().getApplicationInformation(mPackageName, mStoreId, mStoreToken);
	}
}
