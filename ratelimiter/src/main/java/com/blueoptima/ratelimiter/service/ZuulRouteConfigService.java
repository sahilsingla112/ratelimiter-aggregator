package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.exception.ZuulConfigNotUpdatedException;
import com.blueoptima.ratelimiter.model.ApiRegistrationReq;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
public interface ZuulRouteConfigService {

	void refreshZuulConfig(String zuulRouteId) throws ZuulConfigNotUpdatedException;
}
