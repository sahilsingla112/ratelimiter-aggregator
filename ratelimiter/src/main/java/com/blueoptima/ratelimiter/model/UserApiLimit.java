package com.blueoptima.ratelimiter.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 06-06-2020
 */

@Entity
public class UserApiLimit {

	@EmbeddedId
	private UserApiKey userApiKey;

	@NotNull
	private Integer ratelimit;

	public UserApiLimit() {
	}

	public UserApiLimit(UserApiKey userApiKey, @NotNull Integer ratelimit) {
		this.userApiKey = userApiKey;
		this.ratelimit = ratelimit;
	}

	public UserApiKey getUserApiKey() {
		return userApiKey;
	}

	public void setUserApiKey(UserApiKey userApiKey) {
		this.userApiKey = userApiKey;
	}

	public Integer getRatelimit() {
		return ratelimit;
	}

	public void setRatelimit(Integer ratelimit) {
		this.ratelimit = ratelimit;
	}
}
