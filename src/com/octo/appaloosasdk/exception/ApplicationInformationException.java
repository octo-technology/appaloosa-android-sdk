package com.octo.appaloosasdk.exception;

/**
 * Occurs when the sdk failed to retrieve application informations
 * 
 * @author Jerome Van Der Linden
 * 
 */
public class ApplicationInformationException extends AppaloosaException {

	private static final long serialVersionUID = 990966506494410100L;

	public ApplicationInformationException() {
		super();
	}

	public ApplicationInformationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationInformationException(String message) {
		super(message);
	}

	public ApplicationInformationException(Throwable cause) {
		super(cause);
	}

}
