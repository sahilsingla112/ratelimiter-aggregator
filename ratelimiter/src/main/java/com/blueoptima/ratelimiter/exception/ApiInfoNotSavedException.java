package com.blueoptima.ratelimiter.exception;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
public class ApiInfoNotSavedException extends Exception {
	public ApiInfoNotSavedException() {
		super();
	}

	public ApiInfoNotSavedException(String message) {
		super(message);
	}
}
