package com.octo.appaloosasdk.exception;

/**
 * Mother class of all Appaloosa exceptions
 * 
 * @author Jerome Van Der Linden
 * 
 */
public abstract class AppaloosaException extends RuntimeException {

	private static final long serialVersionUID = -7297288872864498191L;

	public AppaloosaException() {
		super();
	}

	public AppaloosaException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppaloosaException(String message) {
		super(message);
	}

	public AppaloosaException(Throwable cause) {
		super(cause);
	}

}
