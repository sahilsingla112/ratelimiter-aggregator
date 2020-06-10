package com.blueoptima.ratelimiter.repository;

import com.blueoptima.ratelimiter.model.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
@Repository
public interface ApiInfoRepository extends JpaRepository<ApiInfo, Long> {

}
