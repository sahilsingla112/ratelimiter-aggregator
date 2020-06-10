package com.blueoptima.ratelimiter.repository;

import com.blueoptima.ratelimiter.model.UserApiKey;
import com.blueoptima.ratelimiter.model.UserApiLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 06-06-2020
 */

@Repository
public interface UserApiLimitRepository extends JpaRepository<UserApiLimit, UserApiKey> {
}
