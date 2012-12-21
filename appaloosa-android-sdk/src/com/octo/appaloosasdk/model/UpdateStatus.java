package com.octo.appaloosasdk.model;

public enum UpdateStatus {

	INITIALIZED(0),
	GETTING_APPLICATION_INFO(1),
	GETTING_APPLICATION_BINARY_URL(2),
	DOWNLOADING(3),
	INSTALLING(4);

	private int value;

	private UpdateStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
