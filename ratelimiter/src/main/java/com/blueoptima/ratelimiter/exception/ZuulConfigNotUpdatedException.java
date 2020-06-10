package com.blueoptima.ratelimiter.exception;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 08-06-2020
 */
public class ZuulConfigNotUpdatedException extends Exception {
	public ZuulConfigNotUpdatedException() {
		super();
	}

	public ZuulConfigNotUpdatedException(String message) {
		super(message);
	}
}
