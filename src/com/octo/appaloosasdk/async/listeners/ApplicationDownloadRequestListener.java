package com.octo.appaloosasdk.async.listeners;

import java.io.InputStream;

import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.appaloosasdk.Appaloosa;

/**
 * Listener used to know the result of the {@link Appaloosa#downloadApplicaton} method
 * 
 * @author Jerome Van Der Linden
 * 
 */
public abstract class ApplicationDownloadRequestListener implements RequestListener<InputStream>, RequestProgressListener {

	/**
	 * {@inheritDoc}
	 */
	public void onRequestProgressUpdate(RequestProgress progress) {

	}

	/**
	 * {@inheritDoc}
	 */
	public abstract void onRequestSuccess(InputStream result);

	/**
	 * {@inheritDoc}
	 */
	public void onRequestFailure(SpiceException spiceException) {
		Log.e("APPALOOSA", "Unable to download application", spiceException);
	}

}
