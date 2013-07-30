package com.octo.appaloosasdk.async.listeners;

import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.appaloosasdk.Appaloosa;
import com.octo.appaloosasdk.exception.AppaloosaException;

/**
 * Listener used to know the result of the {@link Appaloosa#autoUpdate} method. <br/>
 * See also {@link RequestProgressListener}
 * 
 * @author Jerome Van Der Linden
 * 
 */
public interface ApplicationUpdateListener extends RequestProgressListener {

	/**
	 * Implement this method to notify the user the update succeed
	 * 
	 * @param updated
	 *            true when the application is updated, false if the user refuse to install
	 */
	public void onRequestSuccess(boolean updated);

	/**
	 * Implement this method if you want to manage update errors
	 * 
	 * @param appaloosaException
	 *            the exception occurred during the update
	 */
	public void onRequestFailure(AppaloosaException appaloosaException);

}
