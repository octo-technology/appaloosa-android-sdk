package com.octo.appaloosasdk.utils;

import com.octo.appaloosasdk.Appaloosa;
import com.octo.appaloosasdk.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertDialogUtils {
	
	public static AlertDialog buildPopupQuitOrRetry(final Activity activity, final Appaloosa appaloosaSingleton, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
			.setTitle(activity.getString(R.string.application_authorization_title))
			.setMessage(message)
			.setPositiveButton(activity.getString(R.string.retry_button), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					appaloosaSingleton.checkAuthorizationsAndCallback();
				}
			})
			.setNegativeButton(activity.getString(R.string.quit_button), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					activity.finish(); 
				}
			})
			.setCancelable(false)
			.create();
		alertDialog.setCanceledOnTouchOutside(false);
		return alertDialog;
	}

	public static AlertDialog buildPopupQuit(final Activity activity, final Appaloosa appaloosaSingleton, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
			.setTitle(activity.getString(R.string.application_authorization_title))
			.setMessage(message)
			.setNegativeButton(activity.getString(R.string.quit_button), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
						activity.finish(); 
					}
				})
			.setCancelable(false)
			.create();
		alertDialog.setCanceledOnTouchOutside(false);
		return alertDialog;
	}



}
