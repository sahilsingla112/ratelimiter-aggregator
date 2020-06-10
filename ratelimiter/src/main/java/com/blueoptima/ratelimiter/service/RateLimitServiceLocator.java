package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.model.RateLimitStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 08-06-2020
 */

@Component
public class RateLimitServiceLocator {

	@Autowired
	@Qualifier("tunnable_sliding_window")
	private RateLimitService slidingWindowBasedService;

	@Autowired
	@Qualifier("better_fixed_window")
	private RateLimitService fixedWindowBasedService;

	public RateLimitService getRateLimitService(RateLimitStrategy strategy){
		switch (strategy){
		case TUNABLE_SLIDING_WINDOW:
				return slidingWindowBasedService;
		case BETTER_FIXED_WINDOW:
				return fixedWindowBasedService;
		}

		return fixedWindowBasedService;
	}
}
