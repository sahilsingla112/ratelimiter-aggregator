package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.model.ApiInfo;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
public interface RateLimitService {
	boolean allowRequest(String userId, ApiInfo apiInfo, Integer maxRateLimit);
}
