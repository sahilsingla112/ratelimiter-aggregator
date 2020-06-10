package com.blueoptima.ratelimiter.controller;

import com.blueoptima.ratelimiter.model.*;
import com.blueoptima.ratelimiter.service.AdminRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
@RestController
@RequestMapping(value = "/admin")
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminRegistrationService adminRegistrationService;

	@PostMapping(value = "/api")
	public ResponseEntity<ApiRegistrationResp> register(@RequestBody ApiRegistrationReq request){
		try {
			final ApiRegistrationResp response = adminRegistrationService.register(request);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch (Exception e) {
			LOGGER.error("Error while executing api registration", e);
			return new ResponseEntity<>(new ApiRegistrationResp(e.getMessage(), -1L), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/api")
	public ResponseEntity<ApiInfo> update(@RequestBody ApiInfoUpdateReq request){
		try {
			if (request.getId() == null)
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

			final ApiInfo response = adminRegistrationService.update(request);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch (Exception e) {
			LOGGER.error("Error while updating api info", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/user")
	public ResponseEntity<UserRegistrationResp> register(@RequestBody UserRegistrationReq request){
		try {
			final UserRegistrationResp response = adminRegistrationService.register(request);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch (Exception e) {
			String message = String.format("Error: Registration is unsuccessful. Possible cause: %s", e.getMessage());
			LOGGER.error(message, e);
			return new ResponseEntity<>(new UserRegistrationResp(message), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
