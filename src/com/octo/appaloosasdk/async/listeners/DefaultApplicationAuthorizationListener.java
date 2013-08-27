package com.octo.appaloosasdk.async.listeners;

import com.octo.appaloosasdk.Appaloosa;
import com.octo.appaloosasdk.async.requests.ApplicationAuthorizationRequest;
import com.octo.appaloosasdk.model.ApplicationAuthorization.Status;
import com.octo.appaloosasdk.utils.AlertDialogUtils;

import android.app.Activity;

/**
 * Listener used to know the result of the {@link ApplicationAuthorizationRequest}
 * if it is not overrided by the developer
 * 
 * It will close the application in case of not allowed device
 * 
 * @author Christopher Parola
 * 
 */
public class DefaultApplicationAuthorizationListener implements ApplicationAuthorizationListener  {
	
	private Activity activity;
	private Appaloosa appaloosaSingleton;
	
	public DefaultApplicationAuthorizationListener(Activity activity, Appaloosa appaloosaSingleton) {
		this.activity = activity;
		this.appaloosaSingleton = appaloosaSingleton;
	}
	
	@Override
	public void allow(Status status, String message) { }

	@Override
	public void dontAllow(Status status, String message) {
		if(status == Status.NO_NETWORK) {
			AlertDialogUtils.buildPopupQuitOrRetry(activity, appaloosaSingleton, message).show();
		} else {
			AlertDialogUtils.buildPopupQuit(activity, appaloosaSingleton, message).show();
		}		
	}
}
