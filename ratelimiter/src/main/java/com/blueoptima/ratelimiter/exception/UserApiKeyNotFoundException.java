package com.blueoptima.ratelimiter.exception;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 06-06-2020
 */
public class UserApiKeyNotFoundException extends RuntimeException {
	public UserApiKeyNotFoundException() {
		super();
	}

	public UserApiKeyNotFoundException(String message) {
		super(message);
	}
}
