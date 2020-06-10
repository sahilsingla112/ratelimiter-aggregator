package com.blueoptima.ratelimiter;

import com.blueoptima.ratelimiter.model.ApiInfo;
import com.blueoptima.ratelimiter.model.RateLimitStrategy;
import com.blueoptima.ratelimiter.model.UserApiKey;
import com.blueoptima.ratelimiter.model.UserApiLimit;
import com.blueoptima.ratelimiter.repository.ApiInfoRepository;
import com.blueoptima.ratelimiter.repository.UserApiLimitRepository;
import com.blueoptima.ratelimiter.service.UserApiConfigServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class RatelimiterApplication extends SpringBootServletInitializer implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(RatelimiterApplication.class);

	@Autowired UserApiConfigServiceImpl userApiConfigServiceImpl;

	@Autowired ApiInfoRepository apiInfoRepository;

	@Autowired UserApiLimitRepository userApiLimitRepository;


	@Override protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RatelimiterApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(RatelimiterApplication.class, args);

	}

	@Override public void run(String... args) throws Exception {
		initDb();
		userApiConfigServiceImpl.loadAllConfig();
	}

	private void initDb(){
		ApiInfo apiInfo3 = new ApiInfo("/api/v1/books/available", 3, RateLimitStrategy.TUNABLE_SLIDING_WINDOW, 3);
		ApiInfo apiInfo = new ApiInfo("/api/v1/developers", 10, RateLimitStrategy.BETTER_FIXED_WINDOW, null);
		ApiInfo apiInfo2 = new ApiInfo("/api/v1/organizations", 30, RateLimitStrategy.BETTER_FIXED_WINDOW, null);

		ApiInfo saved3 = apiInfoRepository.save(apiInfo3);
		ApiInfo saved = apiInfoRepository.save(apiInfo);
		ApiInfo saved2 = apiInfoRepository.save(apiInfo2);

		UserApiKey userApiKey = new UserApiKey("user1", saved.getId());
		UserApiKey userApiKey2 = new UserApiKey("user1", saved2.getId());
		UserApiKey userApiKey3 = new UserApiKey("user2", saved.getId());
		UserApiKey userApiKey4 = new UserApiKey("user2", saved2.getId());

		UserApiLimit userApiLimit = new UserApiLimit(userApiKey, 10);
		UserApiLimit userApiLimit2 = new UserApiLimit(userApiKey2, 5);
		UserApiLimit userApiLimit3 = new UserApiLimit(userApiKey3, 15);
		UserApiLimit userApiLimit4 = new UserApiLimit(userApiKey4, 20);

		userApiLimitRepository.save(userApiLimit);
		userApiLimitRepository.save(userApiLimit2);
		userApiLimitRepository.save(userApiLimit3);
		userApiLimitRepository.save(userApiLimit4);
	}
}
