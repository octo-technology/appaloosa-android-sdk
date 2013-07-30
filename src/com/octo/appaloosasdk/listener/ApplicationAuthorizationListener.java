package com.octo.appaloosasdk.listener;

public interface ApplicationAuthorizationListener {

	public abstract void allow(int reason);

	public abstract void dontAllow(int reason);

}