package com.octo.appaloosasdk.async.listeners;

import com.octo.appaloosasdk.model.ApplicationAuthorization.Status;

/**
 * Occurs when the sdk failed to retrieve application informations
 * 
 * @author Jerome Van Der Linden
 * 
 */
public interface ApplicationAuthorizationListener {

	public abstract void allow(Status status, String message);

	public abstract void dontAllow(Status status, String message);

}