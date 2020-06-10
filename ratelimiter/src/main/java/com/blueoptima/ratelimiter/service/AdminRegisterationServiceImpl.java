package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.exception.ApiIdNotFoundException;
import com.blueoptima.ratelimiter.exception.ApiInfoNotSavedException;
import com.blueoptima.ratelimiter.exception.ApiRegistrationUnsuccessfulException;
import com.blueoptima.ratelimiter.exception.ZuulConfigNotUpdatedException;
import com.blueoptima.ratelimiter.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service let the administrator to change the underlying configuration of existing users and APIs.
 * It also lets him to register a new API on the fly. System will be refreshed dynamically without restart and start
 * applying rate limiting stratgies based on the configuration.
 * **PRECONDITION**: Zuul routes need to be updated in ratelimiter-dev.properties config file before hitting this API.
 *
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
@Service
public class AdminRegisterationServiceImpl implements AdminRegistrationService{

	private static final String REGISTRATION_IS_SUCCESSUL = "Registration is successul";

	@Autowired
	private UserApiConfigService userApiConfigService;

	@Autowired
	private ZuulRouteConfigService zuulRouteConfigService;

	@Override public ApiInfo update(ApiInfoUpdateReq updateReq) throws ApiInfoNotSavedException {
		ApiInfo apiInfo = userApiConfigService.getApiInfo(updateReq.getId());

		if (updateReq.getRateLimitStrategy() != null) {
			apiInfo.setRateLimitStrategy(updateReq.getRateLimitStrategy());
		}

		if (updateReq.getDefaultLimitPerMinute() != null){
			apiInfo.setRatelimit(updateReq.getDefaultLimitPerMinute());
		}

		if (updateReq.getAccuracyLevel() != null){
			apiInfo.setAccuracyLevel(updateReq.getAccuracyLevel());
		}

		// Add the new api to configuration
		return userApiConfigService.saveApiInfo(apiInfo);
	}

	@Override public ApiRegistrationResp register(ApiRegistrationReq registrationReq) throws ApiRegistrationUnsuccessfulException{
		try {
			//Expectation is that the new route has been added to the Zuul Configuration
			final ApiInfo apiInfo = userApiConfigService.getApiInfo(registrationReq.getDownStreamApiUri());

			if (apiInfo != null)
				throw new ApiRegistrationUnsuccessfulException(String.format("API with id %d already exists. Please PUT method for update", apiInfo.getId()));

			ApiInfo newApiInfo = new ApiInfo(registrationReq.getDownStreamApiUri(), registrationReq.getDefaultLimitPerMinute(),
					registrationReq.getRateLimitStrategy(), registrationReq.getAccuracyLevel());

			// Add the new api to configuration
			ApiInfo saved = userApiConfigService.saveApiInfo(newApiInfo);

			// Dynamic refresh of route configuration for immediate effect
			if (registrationReq.isRefresh())
				zuulRouteConfigService.refreshZuulConfig(registrationReq.getName());

			return new ApiRegistrationResp(REGISTRATION_IS_SUCCESSUL, saved.getId());
		}catch (ZuulConfigNotUpdatedException | ApiInfoNotSavedException e){
			throw new ApiRegistrationUnsuccessfulException(e.getMessage(), e);
		}
	}

	@Override public UserRegistrationResp register(UserRegistrationReq userRegistrationReq) throws
			ApiIdNotFoundException {
		final Integer rateLimitPerSecond = userRegistrationReq.getRateLimitPerMinute();
		final String username = userRegistrationReq.getUsername();
		userApiConfigService.addUserApiInfo(userRegistrationReq.getApiId(), username, rateLimitPerSecond);
		return new UserRegistrationResp(REGISTRATION_IS_SUCCESSUL);
	}
}
