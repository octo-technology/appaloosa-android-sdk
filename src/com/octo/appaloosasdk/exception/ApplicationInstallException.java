package com.octo.appaloosasdk.exception;

/**
 * Occurs when the install of the APK fail
 * 
 * @author Jerome Van Der Linden
 * 
 */
public class ApplicationInstallException extends AppaloosaException {

	private static final long serialVersionUID = -4011372756003993451L;

	public ApplicationInstallException() {
		super();
	}

	public ApplicationInstallException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationInstallException(String message) {
		super(message);
	}

	public ApplicationInstallException(Throwable cause) {
		super(cause);
	}

}
