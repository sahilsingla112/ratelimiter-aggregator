package com.blueoptima.ratelimiter.model;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
public class UserRegistrationReq {
	private Long apiId;
	private String username;

	// Number of requests allowed per second.
	private Integer rateLimitPerMinute;

	// TODO: Future support: Number of requests allowed per hour.
	// private Integer rateLimitPerHour;

	public Long getApiId() {
		return apiId;
	}

	public void setApiId(Long apiId) {
		this.apiId = apiId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getRateLimitPerMinute() {
		return rateLimitPerMinute;
	}

	public void setRateLimitPerMinute(Integer rateLimitPerMinute) {
		this.rateLimitPerMinute = rateLimitPerMinute;
	}
}
