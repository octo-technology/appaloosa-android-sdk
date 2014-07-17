package com.octo.appaloosasdk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import com.octo.appaloosasdk.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestStatus;
import com.octo.android.robospice.request.simple.BigBinaryRequest;
import com.octo.appaloosasdk.async.AppaloosaSpiceService;
import com.octo.appaloosasdk.async.listeners.ApplicationAuthorizationListener;
import com.octo.appaloosasdk.async.listeners.ApplicationAuthorizationRequestListener;
import com.octo.appaloosasdk.async.listeners.ApplicationDownloadRequestListener;
import com.octo.appaloosasdk.async.listeners.ApplicationInformationRequestListener;
import com.octo.appaloosasdk.async.listeners.ApplicationUpToDateListener;
import com.octo.appaloosasdk.async.listeners.ApplicationUpdateListener;
import com.octo.appaloosasdk.async.listeners.DefaultApplicationAuthorizationListener;
import com.octo.appaloosasdk.async.requests.ApplicationAuthorizationRequest;
import com.octo.appaloosasdk.async.requests.ApplicationBinaryUrlRequest;
import com.octo.appaloosasdk.async.requests.ApplicationInformationRequest;
import com.octo.appaloosasdk.exception.AppaloosaException;
import com.octo.appaloosasdk.exception.ApplicationDownloadException;
import com.octo.appaloosasdk.exception.ApplicationInformationException;
import com.octo.appaloosasdk.exception.ApplicationInstallException;
import com.octo.appaloosasdk.model.Application;
import com.octo.appaloosasdk.model.ApplicationAuthorization;
import com.octo.appaloosasdk.model.ApplicationAuthorization.Status;
import com.octo.appaloosasdk.model.DownloadUrl;
import com.octo.appaloosasdk.model.UpdateStatus;
import com.octo.appaloosasdk.ui.activity.AppaloosaDevPanelActivity;
import com.octo.appaloosasdk.utils.DeviceInfo;
import com.octo.appaloosasdk.utils.SystemUtils;

/**
 * Entry point of the Appaloosa SDK. This class is a singleton, use it with <code>Appaloosa.getInstance()</code><br/>
 * To use this SDK, it is necessary to have an account on <a href="http://appaloosa-store.com">http://appaloosa-store.com</a>. <br />
 * On the administration console, in store settings (http://appaloosa-store.com/2012-mystore/store_settings), the <b>store token</b> and <b>store id</b> wil be necessary to use the SDK.
 * 
 * @author Jerome Van Der Linden & Christopher Parola
 * 
 */
public class Appaloosa {
	public static final String APK_BINARY_KEY = "APPALOOSA_APK_BINARY_KEY";

	private static final String TAG_APPALOOSA = "APPALOOSA";
	private static final String UPDATE_DIALOG_TITLE = "Update available";
	private static final String UPDATE_DIALOG_MESSAGE = "A new version of this application is available. Do you want to update?";

	private UpdateStatus mUpdateStatus;
	private SpiceManager mSpiceManager;
	
	private Activity activity;
	private long storeId;
	private String storeToken;
	private ApplicationAuthorizationListener listener;
	private ProgressDialog progressDialog;
	// Listeners (keep in field for next time called)
	private ApplicationUpdateListener mApplicationUpdateListener;

	// Singleton
	private static Appaloosa instance = new Appaloosa();

	public static Appaloosa getInstance() {
		return instance;
	}

	/**
	 * private for singleton
	 */
	private Appaloosa() {
		mSpiceManager = new SpiceManager(AppaloosaSpiceService.class);
		mUpdateStatus = UpdateStatus.INITIALIZED;
	}

	/**
	 * This method does both the verification of the version with {@link Appaloosa#isApplicationUpToDate} and the download / installation of the latest version available
	 * {@link Appaloosa#downloadLastVersion}<br/>
	 * <b>EXECUTED ASYNCHRONOUSLY (NOT IN UI THREAD)</b> : see <a href="https://github.com/octo-online/robospice">Robospice</a> library
	 * 
	 * @param context
	 *            {@link Context} of the application
	 * @param storeId
	 *            the store identifier available in store settings on the console
	 * @param storeToken
	 *            the store token available in store settings on the console
	 * @param showConfirmationDialog
	 *            if true, a confirmation dialog will be shown to the user, to let him confirm the installation
	 * @param dialogTitleResourceId
	 *            string resource id (e.g R.string.title) of the dialog title. If set to 0, the default title "Update available" will be used
	 * @param dialogMessageResourceId
	 *            string resource id (e.g R.string.message) of the dialog confirmation message. If set to 0, the default message
	 *            "A new version of this application is available. Do you want to update?" will be used
	 * @param listener
	 *            the listener used to retrieve the result of the update. <br/>
	 *            Since the method is asynchronous, use the {@link ApplicationUpdateListener} to know the status of the application update (version check, application download)
	 */
	public void autoUpdate(final Context context, final long storeId, final String storeToken, final boolean showConfirmationDialog, final int dialogTitleResourceId,
			final int dialogMessageResourceId, final ApplicationUpdateListener listener) {

		updateListener(listener);

		if (mUpdateStatus.getValue() > UpdateStatus.INITIALIZED.getValue())
			return;

		// Verify the version of the application
		checkForUpdate(context, storeId, storeToken, new ApplicationUpToDateListener() {

			public void onRequestSuccess(boolean isUpToDate, final long id) {
				// if this is not the latest version, download and install it
				if (isUpToDate == false) {

					if (showConfirmationDialog) {
						Log.d(TAG_APPALOOSA, "Show install confirm dialog");
						showConfirmationDialog(context, storeId, storeToken, id, dialogTitleResourceId, dialogMessageResourceId, listener);
					}
					else {
						downloadAndInstallApplication(context, storeId, storeToken, id, listener);
					}
				}
			}

			public void onRequestFailure(AppaloosaException e) {
				mUpdateStatus = UpdateStatus.INITIALIZED;
				if (listener != null) {
					listener.onRequestFailure(e);
				}
			}
		});
	}

	/**
	 * This method does both the verification of the version with {@link Appaloosa#isApplicationUpToDate} and the download / installation of the latest version available
	 * {@link Appaloosa#downloadLastVersion}<br/>
	 * This is the simplest way to auto-update the application : show the confirmation dialog with default text (english version)<br/>
	 * <b>EXECUTED ASYNCHRONOUSLY (NOT IN UI THREAD)</b> : if you want to handle the result, use {@link #autoUpdate(Context, long, String, boolean, int, int, ApplicationUpdateListener)} with the
	 * listener to be notified of the result
	 * 
	 * @param context
	 *            {@link Context} of the application
	 * @param storeId
	 *            the store identifier available in store settings on the console
	 * @param storeToken
	 *            the store token available in store settings on the console
	 */
	public void autoUpdate(final Context context, final long storeId, final String storeToken) {
		autoUpdate(context, storeId, storeToken, true, 0, 0, null);
	}

	/**
	 * This method verify if the current application is the last version available on Appaloosa Store <br/>
	 * <b>EXECUTED ASYNCHRONOUSLY (NOT IN UI THREAD)</b> : see <a href="https://github.com/octo-online/robospice">Robospice</a> library
	 * 
	 * @param context
	 *            {@link Context} of the application
	 * @param storeId
	 *            the store identifier available in store settings on the console
	 * @param storeToken
	 *            the store token available in store settings on the console
	 * @param listener
	 *            the listener used to retrieve the result of the verification. <br/>
	 *            Since the method is asynchronous, use the {@link ApplicationUpToDateListener} to know if the version of the installed application is the same as the version on Appaloosa Store (true)
	 *            and the application id to launch the download
	 */
	public void checkForUpdate(final Context context, long storeId, String storeToken, final ApplicationUpToDateListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener should not be null");
		}

		if (context == null) {
			throw new IllegalArgumentException("context should not be null");
		}

		// retrieve Appaloosa application information
		getApplicationInformation(context, storeId, storeToken, new ApplicationInformationRequestListener() {

			@Override
			public void onRequestFailure(SpiceException spiceException) {
				super.onRequestFailure(spiceException);
				listener.onRequestFailure(new ApplicationInformationException(spiceException));
				mUpdateStatus = UpdateStatus.INITIALIZED;
			}

			@Override
			public void onRequestSuccess(Application application) {
				// get the available version of the application on Appaloosa
				int versionAppaloosa = 0;
				if (application == null) {
					listener.onRequestFailure(new ApplicationInformationException("Application does not exist on Appaloosa"));
				}
				try {
					versionAppaloosa = Integer.parseInt(application.getVersion());
					Log.d(TAG_APPALOOSA, "application version on Appaloosa: " + versionAppaloosa);
				}
				catch (Exception e) {
					listener.onRequestFailure(new ApplicationInformationException("Unable to get Appaloosa application version", e));
				}

				// get the current installed version of the application
				int currentVersion = 0;
				try {
					currentVersion = SystemUtils.getApplicationVersionCode(context);
					Log.d(TAG_APPALOOSA, "installed application version: " + currentVersion);
				}
				catch (NameNotFoundException e) {
					listener.onRequestFailure(new ApplicationInformationException("Unable to get current application version", e));
				}

				if (versionAppaloosa == currentVersion) {
					Log.i(TAG_APPALOOSA, "application is up to date, no need to update");
				}
				else {
					Log.i(TAG_APPALOOSA, "application is not up to date, need to update");
				}
				// application is uptodate if version installed is the same as version on Appaloosa
				listener.onRequestSuccess(versionAppaloosa == currentVersion, application.getId());
			}
		});
	}

	/**
	 * This method retrieve Appaloosa informations about the current {@link Application} in which the SDK is used.<br/>
	 * <i>This method should normally not be called directly by the application (used internally by {@link #isApplicationUpToDate} )</i><br/>
	 * <br/>
	 * <b>EXECUTED ASYNCHRONOUSLY (NOT IN UI THREAD)</b> : see <a href="https://github.com/octo-online/robospice">Robospice</a> library
	 * 
	 * @param context
	 *            {@link Context} of the application
	 * @param storeId
	 *            the store identifier available in store settings on the console
	 * @param storeToken
	 *            the store token available in store settings on the console
	 * @param listener
	 *            the listener used to retrieve the application information. <br/>
	 *            Since the method is asynchronous, use the {@link ApplicationInformationRequestListener} to retrieve the application information
	 */
	public void getApplicationInformation(Context context, long storeId, String storeToken, ApplicationInformationRequestListener listener) {
		String packageName = SystemUtils.getApplicationPackage(context);

		if (!mSpiceManager.isStarted()) {
			mSpiceManager.start(context);
		}
		mUpdateStatus = UpdateStatus.GETTING_APPLICATION_INFO;
		mSpiceManager.execute(new ApplicationInformationRequest(packageName, storeId, storeToken), listener);
	}

	public void downloadAndInstallApplication(final Context context, final long storeId, final String storeToken, final long id, final ApplicationUpdateListener listener) {
		updateListener(listener);

		downloadApplicaton(context, storeId, storeToken, id, new ApplicationDownloadRequestListener() {

			@Override
			public void onRequestSuccess(InputStream result) {
				Log.i(TAG_APPALOOSA, "Application successfully downloaded");
				installApplication(context, storeId, id, result, mApplicationUpdateListener);
			}

			@Override
			public void onRequestProgressUpdate(RequestProgress progress) {
				if (!progress.getStatus().equals(RequestStatus.COMPLETE)) {
					Log.v(TAG_APPALOOSA, (progress.getProgress() * 100) + "/100");
				}

				if (mApplicationUpdateListener != null) {
					mApplicationUpdateListener.onRequestProgressUpdate(progress);
				}
			}

			@Override
			public void onRequestFailure(SpiceException spiceException) {
				String error = "Application download failed";
				Log.e(TAG_APPALOOSA, error, spiceException);
				mUpdateStatus = UpdateStatus.INITIALIZED;

				if (mApplicationUpdateListener != null) {
					mApplicationUpdateListener.onRequestFailure(new ApplicationDownloadException(error, spiceException));
				}
			}
		});
	}

	/**
	 * This method download the latest binary of the application.<br/>
	 * It does not install the application. See {@link #installApplication} for the installation <br/>
	 * <b>EXECUTED ASYNCHRONOUSLY (NOT IN UI THREAD)</b> : see <a href="https://github.com/octo-online/robospice">Robospice</a> library
	 * 
	 * @param context
	 *            {@link Context} of the application
	 * @param storeId
	 *            the store identifier available in store settings on the console
	 * @param storeToken
	 *            the store token available in store settings on the console
	 * @param applicationId
	 *            id of the Application: {@link Application#getId()}
	 * @param listener
	 *            the listener used to retrieve the stream of the binary<br />
	 *            Since the method is asynchronous, use the {@link ApplicationDownloadRequestListener} to get the result of the download
	 */
	public void downloadApplicaton(final Context context, final long storeId, String storeToken, final long applicationId, final ApplicationDownloadRequestListener listener) {
		if (!mSpiceManager.isStarted()) {
			mSpiceManager.start(context);
		}

		mUpdateStatus = UpdateStatus.GETTING_APPLICATION_BINARY_URL;
		// Indeed, this method retrieve the download URL and delegate to downloadApplicationBinary method if succeed
		ApplicationBinaryUrlRequest request = new ApplicationBinaryUrlRequest(storeId, applicationId, storeToken);
		mSpiceManager.execute(request, new RequestListener<DownloadUrl>() {

			public void onRequestFailure(SpiceException spiceException) {
				mUpdateStatus = UpdateStatus.INITIALIZED;
				if (listener != null) {
					listener.onRequestFailure(spiceException);
				}
			}

			public void onRequestSuccess(DownloadUrl result) {
				Log.d(TAG_APPALOOSA, "APK download url=" + result.getDownloadUrl());
				downloadApplicatonBinary(context, result.getDownloadUrl(), storeId, applicationId, listener);
			}

		});
	}

	/**
	 * This method install an application provided in the inputstream parameter.<br />
	 * WARNING : This method close the application just before the system install
	 * 
	 * @param context
	 *            {@link Context} of the application
	 * @param storeId
	 *            the store identifier available in store settings on the console
	 * @param applicationId
	 *            id of the Application: {@link Application#getId()}
	 * @param applicationStream
	 *            the {@link InputStream} containing the apk file of the application
	 * @param listener
	 */
	public void installApplication(Context context, long storeId, long applicationId, InputStream applicationStream, ApplicationUpdateListener listener) {
		File apk = writeFileToDisk(storeId, applicationId, applicationStream, listener);

		if (apk != null && apk.exists() && apk.length() > 0) {
			Log.d(TAG_APPALOOSA, "Installing application (" + apk.getName() + ", " + apk.length() / 1000 + "kb)");
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
			context.startActivity(intent);
			mUpdateStatus = UpdateStatus.INITIALIZED;
			if (listener != null) {
				listener.onRequestSuccess(true);
			}

			// close the application, otherwise, the installation may fail (if start activitity is not the same as previous for example)
			System.exit(0);
		}
	}
	
	/**
	 * This method verify if the device is allowed to launch the application <br/>
	 * It calls checkAuthorizationsAndCallback to check the authorisation
	 * <b>EXECUTED ASYNCHRONOUSLY (NOT IN UI THREAD)</b> : see <a href="https://github.com/octo-online/robospice">Robospice</a> library
	 * @param checkedActivity
	 *	 		  activity which is calling checkAuthorizations (context of the application)
	 * @param storeId
	 *            the store identifier available in store settings on the console
	 * @param storeToken
	 * 			  the store token available in store settings on the console
	 * @param listener
	 *            the listener of the authorization process
	 */
	public void checkAuthorizations(Activity checkedActivity, long storeId, String storeToken, final ApplicationAuthorizationListener listener) {
		this.activity = checkedActivity;
		this.storeId = storeId;
		this.storeToken = storeToken;
		this.listener = listener;
		progressDialog = ProgressDialog.show(checkedActivity, "", checkedActivity.getString(R.string.loading_authorization_message), true);
		
		if (!mSpiceManager.isStarted()) {
			mSpiceManager.start(checkedActivity);
		}
		
		checkAuthorizationsAndCallback();
	}
	
	/**
	 * This method verify if the device is allowed to launch the application <br/>
	 * It create a DefaultApplicationAuthorizationListener to handle the action in case of allowed or not allowed device.
	 * <b>EXECUTED ASYNCHRONOUSLY (NOT IN UI THREAD)</b> : see <a href="https://github.com/octo-online/robospice">Robospice</a> library
	 * WARNING : Default listener will close the application in case of not allowed device
	 * @param checkedActivity
	 *	 		  activity which is calling checkAuthorizations (context of the application)
	 * @param storeId
	 *            the store identifier available in store settings on the console
	 * @param storeToken
	 * 			  the store token available in store settings on the console
	 */
	public void checkAuthorizations(Activity checkedActivity, long storeId, String storeToken) {
		this.activity = checkedActivity;
		this.storeId = storeId;
		this.storeToken = storeToken;
		checkAuthorizations(checkedActivity, storeId, storeToken, new DefaultApplicationAuthorizationListener(activity, this));
	}
	
	/**
	 * This method launch the checkAuthorizations request with right params and use the listener to give the answer
	 */
	public void checkAuthorizationsAndCallback() {

		String packageName = SystemUtils.getApplicationPackage(activity);
		int versionCode = 0;
		try {
			versionCode = SystemUtils.getApplicationVersionCode(activity);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		if(!SystemUtils.checkInternetConnection(activity)) {
			progressDialog.dismiss();
			ApplicationAuthorization authorization = SystemUtils.getBlacklistStatusFromFile(this.activity);
			Status status = Status.valueOf(authorization.getStatus());
			if(status == Status.NOT_AUTHORIZED) {
				listener.dontAllow(status, authorization.getMessage());
			}
		} else {
			String imei = DeviceInfo.getDeviceId(activity);
			String encryptedImei = Base64.encodeToString(imei.getBytes(), Base64.DEFAULT);
			Locale locale = activity.getResources().getConfiguration().locale;
			
			mSpiceManager.execute(new ApplicationAuthorizationRequest(packageName, versionCode, storeId, storeToken, encryptedImei, locale.getLanguage()), new ApplicationAuthorizationRequestListener() {
				
				@Override
				public void onRequestSuccess(ApplicationAuthorization result) {
					progressDialog.dismiss();
					Status status = null;
					try {
						status = Status.valueOf(result.getStatus());
					} catch (IllegalArgumentException e) {
						status = Status.UNKNOWN;
					}
					if (status == Status.AUTHORIZED) {
						listener.allow(Status.AUTHORIZED, result.getMessage());
						SystemUtils.setBlacklistStatusToFile(status, Appaloosa.getInstance().activity);
					}else {
						listener.dontAllow(status, result.getMessage());
						SystemUtils.setBlacklistStatusToFile(status, Appaloosa.getInstance().activity);
					}
					mSpiceManager.shouldStop();
				}
				
				@Override
				public void onRequestFailure(SpiceException spiceException) {
					listener.dontAllow(Status.REQUEST_ERROR, activity.getString(R.string.connection_error_message));
					mSpiceManager.shouldStop();
				}
			});
		}
	}

	// ============================================================================================
	// PRIVATE
	// ============================================================================================
	private File writeFileToDisk(long storeId, long applicationId, InputStream applicationStream, ApplicationUpdateListener listener) {
		// Verify external storage availability
		if (!SystemUtils.isExternalStorageAvailable()) {
			String error = "External storage is not available : " + Environment.getExternalStorageState();
			Log.w(TAG_APPALOOSA, error);
			mUpdateStatus = UpdateStatus.INITIALIZED;
			if (listener != null) {
				listener.onRequestFailure(new ApplicationInstallException(error));
				return null;
			}
		}

		// create folder containing downloaded apk
		String folderApk = Environment.getExternalStorageDirectory().getAbsolutePath() + "/appaloosa/apks/";
		File folderApkFile = new File(folderApk);
		folderApkFile.mkdirs();
		if (!folderApkFile.exists()) {
			String error = "Apk folder does not exist, unable to create it";
			Log.e(TAG_APPALOOSA, error);
			mUpdateStatus = UpdateStatus.INITIALIZED;
			if (listener != null) {
				listener.onRequestFailure(new ApplicationInstallException(error));
				return null;
			}
		}

		// store the apk in the apks folder
		String filename = "temp_binary_" + storeId + "_" + applicationId + ".apk";
		File apk = new File(folderApkFile, filename);
		Log.i(TAG_APPALOOSA, "Saving apk file to " + apk.getAbsolutePath());
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(apk);
			IOUtils.copy(applicationStream, outputStream);
		}
		catch (Exception e) {
			String error = "Unable to create apk file";
			Log.e(TAG_APPALOOSA, error);
			mUpdateStatus = UpdateStatus.INITIALIZED;
			if (listener != null) {
				listener.onRequestFailure(new ApplicationInstallException(error));
				return null;
			}
		}
		finally {
			IOUtils.closeQuietly(outputStream);
		}
		return apk;
	}

	private void showConfirmationDialog(final Context context, final long storeId, final String storeToken, final long applicationId, int dialogTitleResourceId, int dialogMessageResourceId,
			final ApplicationUpdateListener listener) {
		String title = (dialogTitleResourceId == 0 ? UPDATE_DIALOG_TITLE : context.getString(dialogTitleResourceId));
		String message = (dialogMessageResourceId == 0 ? UPDATE_DIALOG_MESSAGE : context.getString(dialogMessageResourceId));

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
		alertBuilder.setMessage(message).setTitle(title).setCancelable(false);

		alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				downloadAndInstallApplication(context, storeId, storeToken, applicationId, listener);
				dialog.cancel();
			}

		});
		alertBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (mApplicationUpdateListener != null) {
					mApplicationUpdateListener.onRequestSuccess(false);
				}
				mUpdateStatus = UpdateStatus.INITIALIZED;
				dialog.cancel();
			}

		});

		AlertDialog alert = alertBuilder.create();
		alert.show();
	}

	private void downloadApplicatonBinary(Context context, String URL, long storeId, long applicationId, ApplicationDownloadRequestListener listener) {
		if (!mSpiceManager.isStarted()) {
			mSpiceManager.start(context);
		}

		mUpdateStatus = UpdateStatus.DOWNLOADING;
		BigBinaryRequest downloadRequest = new BigBinaryRequest(URL, new File(context.getCacheDir(), "temp_binary_" + storeId + "_" + applicationId));
		mSpiceManager.execute(downloadRequest, APK_BINARY_KEY, DurationInMillis.ALWAYS_EXPIRED, listener);

	}

	private synchronized void updateListener(ApplicationUpdateListener listener) {
		mApplicationUpdateListener = listener;
	}
	
	/**
	 * This method launched an activity displaying system and application information 
	 * (OS, screen, package, requested permissions, signatures, permissions, and services information)
	 * 
	 * @param context
	 *            {@link Context} of the application
	 */
	public void displayDevPanel(Context context) {
		Intent intent = new Intent(context, AppaloosaDevPanelActivity.class);
		context.startActivity(intent);
	}

}
