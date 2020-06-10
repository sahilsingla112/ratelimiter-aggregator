package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.model.ApiInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Much better than naive Fixed window algorithm
 * Traffic burst is smoothened by extrapolating the number of requests from the previous window.
 * Example: Window size is 1 minute.
 *					 (hh:mm:ss)
 * 20 requests is made between 10:00:00 and 10:00:59
 * In the previous window, count=20.
 * Then, a request is made at 10:01:14
 * Current Window: 14 seconds
 * Previous Window: 46 seconds
 * Extrapolated requests =  (20 * 46)/60  ~ 15 requests
 * Total requests = 15 + 1 = 16 requests
 *
 *
 * Cost analysis:
 * 2 GET calls (for current and previous window)
 * 1 INCR call
 *
 * Memory analysis:
 * Key size = 16 bytes + 4 bytes + 5 bytes (overhead of delimiters) = 25 bytes
 * Value size = 4 bytes
 * Size of 1 record = 29 bytes
 * 10 million of records will take = 290 MB
 *
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
@Service("better_fixed_window")
public class RateLimitImprovedFixedWindowImpl implements RateLimitService{

	private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitImprovedFixedWindowImpl.class);

	@Autowired
	private JedisPool jedisPool;

	@Override public boolean allowRequest(String userId, ApiInfo apiInfo, Integer maxRateLimit) {
		int totalCount = 0;
		long apiId = apiInfo.getId();

		LOGGER.info("maxRateLimit: " + maxRateLimit);
		try(Jedis jedis = jedisPool.getResource()) {
			final int millisToMinute = 60 * 1000;
			final int millisToSec = 1000;

			// Remove milliseconds from the time
			final long currentTime = System.currentTimeMillis();
			final long currentTimeInSec =(currentTime / millisToSec) * millisToSec;
			final long currentTimeInMinute = (currentTime / millisToMinute) * millisToMinute;

			// Check the timestamp of previous window
			final long prevTimeInMinute = currentTimeInMinute - millisToMinute;

			String currentKey = userId + "_api" + apiId + "_" + currentTimeInMinute;
			String prevKey = userId + "_api" + apiId + "_" + prevTimeInMinute;

			String currentCountStr = jedis.get(currentKey);
			int currentCount  = 0;

			if (currentCountStr != null)
				currentCount = Integer.parseInt(currentCountStr);

			String prevCountStr = jedis.get(prevKey);
			int extrapolatedPrevCount = 0;

			if (prevCountStr != null){
				int secondsOfPrevWindow = (60000 - (int)(currentTimeInSec - currentTimeInMinute))/1000;
				int prevCount = Integer.parseInt(prevCountStr);
				extrapolatedPrevCount = (prevCount * secondsOfPrevWindow)/60;
			}

			totalCount = 1 + currentCount + extrapolatedPrevCount;

			jedis.incr(currentKey);

			// Key didn't exist before
			if (currentCountStr == null) {
				jedis.expire(currentKey, 120);
			}

			LOGGER.info("Count in the current window: " + (1+currentCount));
			LOGGER.info("Extrapolated count from the previous window: " + extrapolatedPrevCount);
		}
		return totalCount <= maxRateLimit;
	}
}
