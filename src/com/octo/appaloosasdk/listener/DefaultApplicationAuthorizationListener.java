package com.octo.appaloosasdk.listener;

import com.octo.appaloosasdk.Appaloosa;
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
	public void allow(int reason){
		
	}
	
	@Override
	public void dontAllow(int reason) {
		if(reason == -1) {
			AlertDialogUtils.showPopupQuitOrRetry(activity, appaloosaSingleton);
		} else {
			AlertDialogUtils.showPopupQuit(activity, appaloosaSingleton, "Message");
		}
	}
}
