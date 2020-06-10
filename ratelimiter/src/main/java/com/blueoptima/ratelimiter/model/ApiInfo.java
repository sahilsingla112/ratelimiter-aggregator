package com.blueoptima.ratelimiter.model;

import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */

@Entity
public class ApiInfo {
	private static final int MAX_ACCURACY_LEVEL = 6;

	@Id
	@GeneratedValue
	private Long id;

	private String url;

	private Integer ratelimit;

	@Column(name = "rate_limit_strategy")
	@ColumnTransformer(read = "UPPER(rate_limit_strategy)", write = "LOWER(?)")
	@Enumerated(EnumType.STRING)
	private RateLimitStrategy rateLimitStrategy;

	@Min(1)
	@Max(6)
	private Integer accuracyLevel;

	public ApiInfo() {
	}

	public ApiInfo(String url, Integer ratelimit, RateLimitStrategy accuracy, Integer accuracyLevel) {
		this.url = url;
		this.ratelimit = ratelimit;
		this.rateLimitStrategy = accuracy;
		this.accuracyLevel = accuracyLevel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getRatelimit() {
		return ratelimit;
	}

	public void setRatelimit(Integer ratelimit) {
		this.ratelimit = ratelimit;
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
