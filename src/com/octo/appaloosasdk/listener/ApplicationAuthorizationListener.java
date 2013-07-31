package com.octo.appaloosasdk.listener;

import com.octo.appaloosasdk.model.ApplicationAuthorization.Status;

public interface ApplicationAuthorizationListener {

	public abstract void allow(Status status, String message);

	public abstract void dontAllow(Status status, String message);

}