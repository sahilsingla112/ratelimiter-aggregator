package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.exception.ApiIdNotFoundException;
import com.blueoptima.ratelimiter.exception.ApiInfoNotSavedException;
import com.blueoptima.ratelimiter.model.ApiInfo;

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

	void addUserApiInfo(Long apiId, String userId, Integer limit) throws ApiIdNotFoundException;

	Integer getRateLimit(String user, String uri);

}
