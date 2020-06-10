package com.blueoptima.ratelimiter.model;

import javax.validation.constraints.NotNull;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 09-06-2020
 */
public class ApiInfoUpdateReq {

	@NotNull
	private Long id;
	private Integer defaultLimitPerMinute;
	private RateLimitStrategy rateLimitStrategy;
	private Integer accuracyLevel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDefaultLimitPerMinute() {
		return defaultLimitPerMinute;
	}

	public void setDefaultLimitPerMinute(Integer defaultLimitPerMinute) {
		this.defaultLimitPerMinute = defaultLimitPerMinute;
	}

	public RateLimitStrategy getRateLimitStrategy() {
		return rateLimitStrategy;
	}

	public void setRateLimitStrategy(RateLimitStrategy rateLimitStrategy) {
		this.rateLimitStrategy = rateLimitStrategy;
	}

	public Integer getAccuracyLevel() {
		return accuracyLevel;
	}

	public void setAccuracyLevel(Integer accuracyLevel) {
		this.accuracyLevel = accuracyLevel;
	}
}
