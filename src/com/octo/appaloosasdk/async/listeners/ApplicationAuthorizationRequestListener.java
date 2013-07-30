package com.octo.appaloosasdk.async.listeners;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.appaloosasdk.async.requests.ApplicationAuthorizationRequest;
import com.octo.appaloosasdk.model.Application;
import com.octo.appaloosasdk.model.ApplicationAuthorization;

/**
 * Listener used to know the result of the {@link ApplicationAuthorizationRequest}
 * 
 * @author Christopher Parola
 * 
 */
public abstract class ApplicationAuthorizationRequestListener implements RequestListener<ApplicationAuthorization> {

	/**
	 * Override this method if you want to do something in case of error
	 */
	public abstract void onRequestFailure(SpiceException spiceException);

	/**
	 * Override this method to manage the {@link Application} retrieved
	 * 
	 * @param result
	 *            the application retrieved asynchronously
	 */
	public abstract void onRequestSuccess(ApplicationAuthorization result);

}
