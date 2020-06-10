package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.model.ApiInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * Modifiied implementation inspired from sliding window algorithm.
 * Time Window will be divided into 60 parts by default. Here, 1 minute will be divided 60 windows of 1sec each.
 * Number of parts can be either 60, 30, or 20. Depending upon the accuracyLevel parameter from api_info table.
 * AccuracyLevel 1 gives highest accuracy, 3 will give relatively lower but throughput will be improved.
 * Key will be combination of User + API Id.
 * Value will be another Redis hash whose key will be Second timestamp and value will be count of requests.
 *
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
@Service("tunnable_sliding_window")
public class RateLimitSlidingWindowImpl implements RateLimitService{

	private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitSlidingWindowImpl.class);

	@Autowired
	private JedisPool jedisPool;

	@Override public boolean allowRequest(String userId, ApiInfo apiInfo, Integer maxRateLimit) {
		int totalCount = 1;
		final long apiId = apiInfo.getId();
		Integer accuracyLevel = apiInfo.getAccuracyLevel();

		// Normalise the accuracy level
		if (accuracyLevel == null || accuracyLevel < 1)
			accuracyLevel = 1;
		else if (accuracyLevel > 3)
			accuracyLevel = 3;

		try (Jedis jedis = jedisPool.getResource()){
			String currentKey = userId + "_api" + apiId;

			final int millisToSec = 1000;

			final long currentTime0 = System.currentTimeMillis();
			final long currentTimeInSec0 = (currentTime0 / millisToSec) * millisToSec;

			final Map<String, String> allWindows = jedis.hgetAll(currentKey);
			for (Map.Entry<String, String> window: allWindows.entrySet()){

				final String key =  window.getKey();
				long time = Long.parseLong(key);

				// Remove all those windows from the hash that are past 1 min.
				if ((currentTime0 - time) > 60000) {
					jedis.hdel(currentKey, key);
				}else{
					long count = Long.parseLong(window.getValue());
					totalCount += count;
				}
			}

			long currentWindow = findWindow(currentTimeInSec0, accuracyLevel);

			jedis.hincrBy(currentKey, Long.toString(currentWindow), 1);

		}catch (Exception e){
			LOGGER.error("Exception while calculating rate limit", e);
		}

		LOGGER.info("Total count: " + totalCount);
		return totalCount <= maxRateLimit;
	}


	private long findWindow(long time, int accuracyLevel){
		if (accuracyLevel == 1)
			return time;
		long timeSeconds = time / 1000;
		return ((timeSeconds/accuracyLevel) * accuracyLevel) * 1000;
	}
}
