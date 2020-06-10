package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.exception.ApiIdNotFoundException;
import com.blueoptima.ratelimiter.exception.ApiInfoNotSavedException;
import com.blueoptima.ratelimiter.model.ApiInfo;
import com.blueoptima.ratelimiter.model.UserApiKey;
import com.blueoptima.ratelimiter.model.UserApiLimit;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
public interface UserApiConfigService {

	void loadAllConfig();

	ApiInfo getApiInfo(Long apiId);

	ApiInfo getApiInfo(String apiUri);

	ApiInfo saveApiInfo(ApiInfo apiInfo) throws ApiInfoNotSavedException;

	UserApiLimit saveUserApiInfo(UserApiKey userApiKey, Integer limit) throws ApiIdNotFoundException;

	void addUserApiInfo(Long apiId, String userId, Integer limit) throws ApiIdNotFoundException;

	Integer getRateLimit(String user, String uri);

}
