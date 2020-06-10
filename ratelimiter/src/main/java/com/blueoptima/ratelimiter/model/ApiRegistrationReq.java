package com.blueoptima.ratelimiter.model;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
public class ApiRegistrationReq {
	private Long id;

	// Example: apiv1dev
	private String name;

	private boolean includeRequestPathInUri = false;

	// Example: http://blueoptima.devapi.com
	private String downStreamServiceUrl;

	// Example: /api/v1/developers
	private String downStreamApiUri;

	// If user+api combination is not available, this limit will apply.
	private Integer defaultLimitPerMinute;

	//TODO: future support
	// private Integer defaultLimitPerHour;

	// This flag will refresh the config data in memory as well as underlying storage
	// This will make the update with immediate effect.
	private boolean refresh;

	private RateLimitStrategy rateLimitStrategy;

	private Integer accuracyLevel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIncludeRequestPathInUri() {
		return includeRequestPathInUri;
	}

	public void setIncludeRequestPathInUri(boolean includeRequestPathInUri) {
		this.includeRequestPathInUri = includeRequestPathInUri;
	}

	public String getDownStreamServiceUrl() {
		return downStreamServiceUrl;
	}

	public void setDownStreamServiceUrl(String downStreamServiceUrl) {
		this.downStreamServiceUrl = downStreamServiceUrl;
	}

	public String getDownStreamApiUri() {
		return downStreamApiUri;
	}

	public void setDownStreamApiUri(String downStreamApiUri) {
		this.downStreamApiUri = downStreamApiUri;
	}

	public Integer getDefaultLimitPerMinute() {
		return defaultLimitPerMinute;
	}

	public void setDefaultLimitPerMinute(Integer defaultLimitPerMinute) {
		this.defaultLimitPerMinute = defaultLimitPerMinute;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
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
