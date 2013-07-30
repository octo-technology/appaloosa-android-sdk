package com.octo.appaloosasdk.utils;

import com.octo.appaloosasdk.Appaloosa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertDialogUtils {
	
	public static void showPopupQuitOrRetry(final Activity activity, final Appaloosa appaloosaSingleton) {
		new AlertDialog.Builder(activity)
		.setTitle("Connexion impossible")
		.setMessage("Vous devez activer votre connexion internet.")
		.setPositiveButton("Re-essayer", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				appaloosaSingleton.checkAuthorizationsAndCallback();
			}
		})
		.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				activity.finish(); 
			}
		})
		.show();
	}

	public static void showPopupQuit(final Activity activity, final Appaloosa appaloosaSingleton, String message) {
		new AlertDialog.Builder(activity)
		.setTitle("Connexion impossible")
		.setMessage(message)
		.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				activity.finish(); 
			}
		})
		.show();
	}



}
