package com.octo.appaloosasdk.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;

import com.octo.appaloosasdk.R;
import com.octo.appaloosasdk.model.ApplicationAuthorization;
import com.octo.appaloosasdk.model.ApplicationAuthorization.Status;
import com.octo.appaloosasdk.model.ConfigProperty;

public class SystemUtils {
	
	private static final String BLACKLIST_FILENAME = "BLACKLIST_STATUS";

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
	
	/**
	 * Return a List of simple information of the app retrieved from the given packageInfo
	 * Retrieved info are : versionName, versionCode, PackageName, firstInstallTime, lastUpdateTime
	 * 
	 * 
	 * @param PackageInfo
	 * @return list of ConfigProperty
	 */
	@SuppressLint("NewApi")
	public static List<ConfigProperty> getPackageInfoDataList(PackageInfo packageInfo) {
		List<ConfigProperty> packageInfoList = new ArrayList<ConfigProperty>();
		
		if (packageInfo == null)
			return packageInfoList;
		
		packageInfoList.add(new ConfigProperty("Package name", packageInfo.packageName));
		packageInfoList.add(new ConfigProperty("App version name", packageInfo.versionName));
		packageInfoList.add(new ConfigProperty("App version code", String.valueOf(packageInfo.versionCode)));
		if (android.os.Build.VERSION.SDK_INT >= 9) {
			packageInfoList.add(new ConfigProperty("First install", String.valueOf(DateFormat.format("dd/MM/yyyy kk:mm", packageInfo.firstInstallTime))));
			packageInfoList.add(new ConfigProperty("Last update", String.valueOf(DateFormat.format("dd/MM/yyyy kk:mm", packageInfo.lastUpdateTime))));
		}
		
		return packageInfoList;
	}
	
	/**
	 * Return a list of requested permissions retrieved from the given packageInfo
	 * 
	 * 
	 * @param PackageInfo
	 * @return list of ConfigProperty
	 */
	public static List<ConfigProperty>getRequestedPermissionsDataList(PackageInfo packageInfo) {
		List<ConfigProperty> packageInfoList = new ArrayList<ConfigProperty>();
		
		if (packageInfo == null || packageInfo.requestedPermissions == null)
			return packageInfoList;
		
    	for (String requestedPermission : packageInfo.requestedPermissions) {
    		packageInfoList.add(new ConfigProperty("Requested permission", requestedPermission.toString()));
    	}
		return packageInfoList;	
	}
	
	/**
	 * Return a list of permissions retrieved from the given packageInfo
	 * 
	 * 
	 * @param PackageInfo
	 * @return list of ConfigProperty
	 */
	public static List<ConfigProperty>getPermissionsDataList(PackageInfo packageInfo) {
		List<ConfigProperty> packageInfoList = new ArrayList<ConfigProperty>();
		
		if (packageInfo == null || packageInfo.permissions == null)
			return packageInfoList;
		
		for (PermissionInfo permission : packageInfo.permissions) {
			packageInfoList.add(new ConfigProperty("Permission", permission.name));
		}
		return packageInfoList;	
	}
	
	/**
	 * Return a list of signatures retrieved from the given packageInfo
	 * 
	 * 
	 * @param PackageInfo
	 * @return list of ConfigProperty
	 */
	public static List<ConfigProperty>getSignaturesDataList(PackageInfo packageInfo) {
		List<ConfigProperty> packageInfoList = new ArrayList<ConfigProperty>();
		
		if (packageInfo == null || packageInfo.signatures == null)
			return packageInfoList;
		
		for (Signature signature : packageInfo.signatures) {
			packageInfoList.add(new ConfigProperty("Signature", signature.toString()));
		}
		return packageInfoList;	
	}
	
	/**
	 * Return a list of services retrieved from the given packageInfo
	 * 
	 * 
	 * @param PackageInfo
	 * @return list of ConfigProperty
	 */
	public static List<ConfigProperty>getServicesDataList(PackageInfo packageInfo) {
		List<ConfigProperty> packageInfoList = new ArrayList<ConfigProperty>();
		
		if (packageInfo == null || packageInfo.services == null)
			return packageInfoList;
		
		for (ServiceInfo serviceInfo : packageInfo.services) {
			packageInfoList.add(new ConfigProperty("Service", serviceInfo.name));
		}
		return packageInfoList;	
	}
	
	/**
	 * Return a list of display metrics info
	 * 
	 * 
	 * @param PackageInfo
	 * @return list of ConfigProperty
	 */
	public static List<ConfigProperty> getDisplayMetricsDataList(Context context) {
		List<ConfigProperty> displayMetricsDataList = new ArrayList<ConfigProperty>();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        displayMetricsDataList.add(new ConfigProperty("Screen density ", String.valueOf(metrics.density)));
        displayMetricsDataList.add(new ConfigProperty("DPI Screen density", String.valueOf(metrics.densityDpi)));
        displayMetricsDataList.add(new ConfigProperty("Screen dimensions", metrics.widthPixels + " x " + metrics.heightPixels));
        return displayMetricsDataList;
	}
	
	/**
	 * Return a list of general app information
	 * 
	 * 
	 * @param Context
	 * @return list of ConfigProperty
	 */
	public static List<ConfigProperty> getGeneralConfigPropertyList(Context context) {
		List<ConfigProperty> generalDataList = new ArrayList<ConfigProperty>();
		
		String device = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
		device = device.substring(0, 1).toUpperCase(Locale.getDefault()) + device.substring(1);
		generalDataList.add(new ConfigProperty("Device Model", device));

		generalDataList.add(new ConfigProperty("Rooted device", isRootedDevice() ? "Yes" : "No"));
		
		generalDataList.add(new ConfigProperty("OS version", android.os.Build.VERSION.RELEASE));
		generalDataList.add(new ConfigProperty("Android API level", String.valueOf(android.os.Build.VERSION.SDK_INT)));

		long ramSize = getRamSizeAvailable(context);
		generalDataList.add(new ConfigProperty("Available RAM size", ramSize == 0 ? "unknown" : "~ " + ramSize + " Mb"));
		
		return generalDataList;
	}
	
	private static long getRamSizeAvailable(Context context) {
		long ramSize = 0;
		ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		actManager.getMemoryInfo(memInfo);
		ramSize = memInfo.availMem / (1024 * 1024);
		return ramSize;
	}
	
	/**
	 * Return a list of config property about external storage
	 * 
	 * 
	 * @return list of ConfigProperty
	 */
	public static List<ConfigProperty> getExternalStorageConfigPropertyList() {
		List<ConfigProperty> storageDataList = new ArrayList<ConfigProperty>();		
		String state = Environment.getExternalStorageState();
		
		if (!Environment.MEDIA_MOUNTED.equals(state) && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return storageDataList;
		}
		
		String storagePath = Environment.getExternalStorageDirectory().getPath();
		StatFs stat = new StatFs(storagePath);
		long mbAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks() / (1024 * 1024);
		long mbTotal = (long)stat.getBlockSize() * (long)stat.getBlockCount() / (1024 * 1024);
		
		storageDataList.add(new ConfigProperty("Directory path", storagePath));
		storageDataList.add(new ConfigProperty("Free", mbAvailable + " Mb"));
		storageDataList.add(new ConfigProperty("Total", mbTotal + " Mb"));
		
		return storageDataList;
	}
	
	/**
	 * Return a list of config property about internal storage
	 * 
	 * 
	 * @return list of ConfigProperty
	 */
	public static List<ConfigProperty> getInternalStorageConfigPropertyList() {
		List<ConfigProperty> storageDataList = new ArrayList<ConfigProperty>();
		
		String storagePath = Environment.getDataDirectory().getPath();
		StatFs stat = new StatFs(storagePath);
		long mbAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks() / (1024 * 1024);
		long mbTotal = (long)stat.getBlockSize() * (long)stat.getBlockCount() / (1024 * 1024);
		
		storageDataList.add(new ConfigProperty("Directory path", storagePath));
		storageDataList.add(new ConfigProperty("Free", mbAvailable + " Mb"));
		storageDataList.add(new ConfigProperty("Total", mbTotal + " Mb"));
		return storageDataList;
	}
	
	/**
	 * Determine if the device is rooted.
	 * 
	 * @return true if the device is rooted, false otherwise.
	 */
	public static boolean isRootedDevice() {

		String[] rootCommands = {
				"su",
				"/system/bin/su",
				"/system/xbin/su",
				"which su",
				"/system/bin/which su",
				"/system/xbin/which su"
				};

		if (isAbleToRunCommand(rootCommands)) {
			return true;
		}

		String tags = android.os.Build.TAGS;
		if (tags != null && tags.contains("test-keys")) {
			return true;
		}

		if (superUserApkExists()) {
			return true;
		}
		
		return false;
	}

	private static boolean isAbleToRunCommand(String[] commands) {

		boolean executedSuccesfully = false;
		for (String command : commands) {
			try {
				Runtime.getRuntime().exec(command);
				executedSuccesfully = true;
			} catch (Exception e) { }
		}

		return executedSuccesfully;
	}

	private static boolean superUserApkExists() {

		try {
			if (new File("/system/app/Superuser.apk").exists()) {
				return true;
			}
		} catch (Exception e) { }
		
		return false;
	}
	
	public static void setBlacklistStatusToFile(Status status, Activity checkedActivity) {
		try {
			String statusString = status.toString();
			FileOutputStream fOS = checkedActivity.openFileOutput(BLACKLIST_FILENAME, Context.MODE_PRIVATE);
			fOS.write(statusString.getBytes());
			fOS.close();
		} catch (IOException e) { }
	}
	
	public static ApplicationAuthorization getBlacklistStatusFromFile(Activity checkedActivity) {
		ApplicationAuthorization applicationAuthorization;
		try {
			FileInputStream fIS = checkedActivity.openFileInput(BLACKLIST_FILENAME);
            InputStreamReader iSR = new InputStreamReader(fIS);
            BufferedReader bR = new BufferedReader(iSR);
            Status status = Status.valueOf(bR.readLine());
            bR.close();
            iSR.close();
            fIS.close();
            applicationAuthorization = getApplicationAuthorizationWithStatus(status, checkedActivity);
		} catch (IOException e) {
			applicationAuthorization = getApplicationAuthorizationWithStatus(Status.AUTHORIZED, checkedActivity);
		}
		return applicationAuthorization;
	}

	private static ApplicationAuthorization getApplicationAuthorizationWithStatus(Status status, Activity checkedActivity) {
		ApplicationAuthorization applicationAuthorization = new ApplicationAuthorization();
		applicationAuthorization.setStatus(status.toString());
		if(status == Status.NOT_AUTHORIZED) {
		    applicationAuthorization.setMessage(checkedActivity.getString(R.string.not_authorized_message));
		}
		return applicationAuthorization;
	}
}
