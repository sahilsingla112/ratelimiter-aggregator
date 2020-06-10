package com.blueoptima.ratelimiter.model;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
public class UserRegistrationResp {
	private String message="user is registered successfully";

	public UserRegistrationResp() {
	}

	public UserRegistrationResp(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
