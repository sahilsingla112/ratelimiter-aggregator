package com.blueoptima.ratelimiter.exception;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 09-06-2020
 */
public class ApiRegistrationUnsuccessfulException extends Exception {
	public ApiRegistrationUnsuccessfulException() {
		super();
	}

	public ApiRegistrationUnsuccessfulException(String message) {
		super(message);
	}

	public ApiRegistrationUnsuccessfulException(String message, Throwable cause) {
		super(message, cause);
	}
}
