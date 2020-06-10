package com.blueoptima.ratelimiter.model;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
public class ApiRegistrationResp {
	private String message="Api is registered successfully";
	private Long apiId;

	public ApiRegistrationResp() {
	}

	public ApiRegistrationResp(String message, Long apiId) {
		this.message = message;
		this.apiId = apiId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getApiId() {
		return apiId;
	}

	public void setApiId(Long apiId) {
		this.apiId = apiId;
	}
}
