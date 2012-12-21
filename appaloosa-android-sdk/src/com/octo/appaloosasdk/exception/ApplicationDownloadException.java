package com.octo.appaloosasdk.exception;

/**
 * Occurs when the download of the APK fail
 * 
 * @author Jerome Van Der Linden
 * 
 */
public class ApplicationDownloadException extends AppaloosaException {

	private static final long serialVersionUID = -44721367539994572L;

	public ApplicationDownloadException() {
		super();
	}

	public ApplicationDownloadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationDownloadException(String message) {
		super(message);
	}

	public ApplicationDownloadException(Throwable cause) {
		super(cause);
	}

}
