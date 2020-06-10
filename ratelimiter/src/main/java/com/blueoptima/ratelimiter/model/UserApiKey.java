package com.blueoptima.ratelimiter.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 06-06-2020
 */
@Embeddable
public class UserApiKey implements Serializable {

	@NotNull
	private String userId;

	@NotNull
	private Long apiId;

	public UserApiKey() {
	}

	public UserApiKey(@NotNull String userId, @NotNull Long apiId) {
		this.userId = userId;
		this.apiId = apiId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getApiId() {
		return apiId;
	}

	public void setApiId(Long apiId) {
		this.apiId = apiId;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserApiKey that = (UserApiKey) o;
		return Objects.equals(userId, that.userId) && Objects.equals(apiId, that.apiId);
	}

	@Override public int hashCode() {
		return Objects.hash(userId, apiId);
	}
}
