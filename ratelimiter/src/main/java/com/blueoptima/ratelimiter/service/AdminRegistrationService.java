package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.exception.ApiIdNotFoundException;
import com.blueoptima.ratelimiter.exception.ApiInfoNotSavedException;
import com.blueoptima.ratelimiter.exception.ApiRegistrationUnsuccessfulException;
import com.blueoptima.ratelimiter.exception.ZuulConfigNotUpdatedException;
import com.blueoptima.ratelimiter.model.*;
import org.springframework.stereotype.Service;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
@Service
public interface AdminRegistrationService {

	ApiInfo update(ApiInfoUpdateReq updateReq) throws ApiInfoNotSavedException;
	ApiRegistrationResp register(ApiRegistrationReq registrationReq) throws ApiRegistrationUnsuccessfulException;
	UserRegistrationResp register(UserRegistrationReq userRegistrationReq) throws ApiIdNotFoundException;

}
