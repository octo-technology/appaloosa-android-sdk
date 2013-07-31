package com.octo.appaloosasdk.listener;

import com.octo.appaloosasdk.Appaloosa;
import com.octo.appaloosasdk.model.ApplicationAuthorization.Status;
import com.octo.appaloosasdk.utils.AlertDialogUtils;

import android.app.Activity;

public class DefaultApplicationAuthorizationListener implements ApplicationAuthorizationListener  {
	
	private Activity activity;
	private Appaloosa appaloosaSingleton;
	
	public DefaultApplicationAuthorizationListener(Activity activity, Appaloosa appaloosaSingleton) {
		this.activity = activity;
		this.appaloosaSingleton = appaloosaSingleton;
	}
	
	@Override
	public void allow(Status status, String message) {
	}

	@Override
	public void dontAllow(Status status, String message) {
		if(status == Status.NO_NETWORK) {
			AlertDialogUtils.buildPopupQuitOrRetry(activity, appaloosaSingleton, message).show();
		} else {
			AlertDialogUtils.buildPopupQuit(activity, appaloosaSingleton, message).show();
		}		
	}
}
