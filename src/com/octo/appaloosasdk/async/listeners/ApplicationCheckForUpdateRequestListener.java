package com.octo.appaloosasdk.async.listeners;

import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.appaloosasdk.Appaloosa;
import com.octo.appaloosasdk.model.Application;
import com.octo.appaloosasdk.model.ApplicationCheckUpdate;

/**
 * Listener used to know the result of the {@link Appaloosa#getApplicationInformation} method
 * 
 * @author Jerome Van Der Linden
 * 
 */
public abstract class ApplicationCheckForUpdateRequestListener implements RequestListener<ApplicationCheckUpdate> {

	/**
	 * Override this method if you want to do something in case of error
	 */
	public void onRequestFailure(SpiceException spiceException) {
		Log.e("APPALOOSA", "Unable to retrieve application informations", spiceException);
	}

	/**
	 * Override this method to manage the {@link Application} retrieved
	 * 
	 * @param result
	 *            the application retrieved asynchronously
	 */
	public abstract void onRequestSuccess(ApplicationCheckUpdate result);

}
