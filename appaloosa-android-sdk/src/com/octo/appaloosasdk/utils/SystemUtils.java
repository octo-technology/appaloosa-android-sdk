package com.octo.appaloosasdk.utils;

import java.util.HashMap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Environment;

public class SystemUtils {

	public static HashMap<Integer, String> osVersion;

	/**
	 * Version of the SDK on the current device
	 * 
	 * @return
	 */
	public static int getSystemVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * System type : for now only "Android", maybe distinguish phone and tablet later
	 * 
	 * @return
	 */
	public static String getSystemType() {
		return "Android";
	}

	/**
	 * Locale of the device
	 * 
	 * @param context
	 * @return
	 */
	public static String getSystemLocale(Context context) {
		return context.getResources().getConfiguration().locale.getLanguage();
	}

	public static final String getApplicationPackage(Context context) {
		return context.getPackageName();
	}

	/**
	 * Get the version code of the store (current application package)
	 * 
	 * @param context
	 * @return version Code of the application
	 * @throws NameNotFoundException
	 *             if application is not found : should not occurred because this is the running application itself
	 */
	public static int getApplicationVersionCode(Context context) throws NameNotFoundException {
		PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		return pinfo.versionCode;
	}

	/**
	 * Get the version name of the store (current application package)
	 * 
	 * @param context
	 * @return version Name of the application
	 * @throws NameNotFoundException
	 *             if application is not found : should not occurred because this is the running application itself
	 */
	public static String getApplicationVersionName(Context context) throws NameNotFoundException {
		PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		return pinfo.versionName;
	}

	/**
	 * Verify the status of the connection (available or not)
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean checkInternetConnection(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isExternalStorageAvailable() {
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageWriteable = true;
		}
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageWriteable = false;
		}
		else {
			// Something else is wrong. It may be one of many other states, but all we need
			// to know is we can neither read nor write
			mExternalStorageWriteable = false;
		}
		return mExternalStorageWriteable;
	}
}
