package com.blueoptima.ratelimiter.config;

import com.blueoptima.ratelimiter.zuul.PreFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
@Configuration
public class ZuulConfig {
	private static final Logger LOG = LoggerFactory.getLogger(RateLimiterConfig.class);

	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}

	@Primary
	@Bean(name = "zuulConfigProperties")
	@RefreshScope
	@ConfigurationProperties("zuul")
	public ZuulProperties zuulProperties() {
		return new ZuulProperties();
	}
}

