package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.exception.ApiIdNotFoundException;
import com.blueoptima.ratelimiter.exception.ApiInfoNotSavedException;
import com.blueoptima.ratelimiter.model.ApiInfo;
import com.blueoptima.ratelimiter.model.UserApiKey;
import com.blueoptima.ratelimiter.model.UserApiLimit;
import com.blueoptima.ratelimiter.repository.ApiInfoRepository;
import com.blueoptima.ratelimiter.repository.UserApiLimitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 06-06-2020
 */

/**
 * Thread safe
 */
@Service
public class UserApiConfigServiceImpl implements UserApiConfigService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserApiConfigServiceImpl.class);

	@Autowired
	private UserApiLimitRepository userApiLimitRepository;

	@Autowired
	private ApiInfoRepository apiInfoRepository;

	private Map<UserApiKey, Integer> userApiLimitMap = new ConcurrentHashMap<>();
	private Map<Long, ApiInfo> apiLimitMap = new ConcurrentHashMap<>();
	private Map<String, Long> apiMap = new ConcurrentHashMap<>();

	@Value("${default.rate.limit:100}")
	private Integer globalDefaultLimit;

	// Store config in memory, .
	@Override
	public void loadAllConfig(){
		final List<UserApiLimit> all = userApiLimitRepository.findAll();
		for (UserApiLimit userApiLimit: all){
			userApiLimitMap.put(userApiLimit.getUserApiKey(), userApiLimit.getRatelimit());
		}

		final List<ApiInfo> apiInfos = apiInfoRepository.findAll();
		for (ApiInfo apiInfo: apiInfos){
			apiLimitMap.put(apiInfo.getId(), apiInfo);
			apiMap.put(apiInfo.getUrl(), apiInfo.getId());
		}
	}

	@Override
	public ApiInfo getApiInfo(String apiUri){
		final Long id = apiMap.get(apiUri);

		if (id != null){
			return getApiInfo(id);
		}

		return null;

	}

	@Override
	public ApiInfo getApiInfo(Long apiId){
		return apiLimitMap.get(apiId);
	}

	@Override
	public ApiInfo saveApiInfo(ApiInfo apiInfo) throws ApiInfoNotSavedException{
		final ApiInfo saved = apiInfoRepository.save(apiInfo);

		if (saved == null)
			throw new ApiInfoNotSavedException("Error in saving API info to configuration");

		apiMap.put(saved.getUrl(), saved.getId());
		apiLimitMap.put(saved.getId(), saved);
		return saved;
	}

	@Override
	public void addUserApiInfo(Long apiId, String userId, Integer limit) throws ApiIdNotFoundException{
		final Optional<ApiInfo> byId = apiInfoRepository.findById(apiId);
		if (!byId.isPresent()) {
			throw new ApiIdNotFoundException(
					String.format("API id %d coult not be found in the configuration, cannot be registered", apiId));
		}
		userApiLimitMap.put(new UserApiKey(userId, apiId), limit);
	}

	/**
	 * First try user+api combination, then api default limit, finally global limit
	 * @param user
	 * @param uri
	 * @return
	 */
	@Override
	public Integer getRateLimit(String user, String uri){
		final Long apiId = apiMap.get(uri);
		UserApiKey userApiKey = new UserApiKey(user, apiId);

		Integer limit = userApiLimitMap.get(userApiKey);

		if (limit == null) {
			limit = getApiInfo(uri).getRatelimit();
			if (limit == null)
				limit = globalDefaultLimit;
		}

		return limit;
	}
}
