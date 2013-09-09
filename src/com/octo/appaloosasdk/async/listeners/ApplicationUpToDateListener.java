package com.octo.appaloosasdk.async.listeners;

import com.octo.appaloosasdk.Appaloosa;
import com.octo.appaloosasdk.exception.AppaloosaException;
import com.octo.appaloosasdk.model.ApplicationCheckUpdate;

/**
 * Listener used to know the result of the {@link Appaloosa#checkForUpdate} method
 * 
 * @author Jerome Van Der Linden
 * 
 */
public interface ApplicationUpToDateListener {

	/**
	 * This method is called when the {@link Appaloosa#checkForUpdate} method succeeded. <br />
	 * In that case, it tell if the application is up to date and provide a technical id to launch further request in case the application must be updated.
	 * 
	 * @param isUpToDate
	 *            either the application is up to date (true) or not
	 * @param id
	 *            the technical id of the application (different from the package name)
	 */
	void onRequestSuccess(ApplicationCheckUpdate applicatonCheckUpdate);

	/**
	 * This method is called when the {@link Appaloosa#checkForUpdate} method failed. <br />
	 * In that case, it returns the {@link AppaloosaException} thrown during the treatment
	 * 
	 * @param e
	 *            exception that occurred
	 */
	void onRequestFailure(AppaloosaException e);

}
