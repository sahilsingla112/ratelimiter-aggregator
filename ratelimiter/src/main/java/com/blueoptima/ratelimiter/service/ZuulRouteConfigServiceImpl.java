package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.exception.ZuulConfigNotUpdatedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
@Service
public class ZuulRouteConfigServiceImpl implements ZuulRouteConfigService{

	private static final Logger LOGGER = LoggerFactory.getLogger(ZuulRouteConfigServiceImpl.class);

	@Autowired
	private ZuulProperties zuulProperties;

	private String zuulServerUrl;
	private String refreshUrl;

	@Value("${server.port}")
	private String port;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	public void init(){
		zuulServerUrl = String.format("%s:%s", "http://localhost", port);
		refreshUrl = String.format("%s/actuator/refresh", zuulServerUrl);
		LOGGER.info(refreshUrl);
	}

	/**
	 * Precondition: Update the new zuul route in configuration file first (ex: ratelimiter-dev.properties).
	 * Spring cloud Config server will automatically pick that new configuration and injected to ZuulProperties by @RefreshScope.
	 * By executing, /actuator/refresh, it will refresh the routing information dynamically without restart required.
	 * @param zuulRouteId
	 * @throws ZuulConfigNotUpdatedException
	 */
	@Override public void refreshZuulConfig(String zuulRouteId) throws ZuulConfigNotUpdatedException{
		String personResultAsJsonStr = restTemplate.postForObject(refreshUrl, null, String.class);
		try {
			final ZuulProperties.ZuulRoute zuulRoute = zuulProperties.getRoutes().get(zuulRouteId);
			if (zuulRoute != null) {
				LOGGER.info("Refresh response: " + personResultAsJsonStr);
			}else{
				String msg = String.format("Zuul config with %s is not updated. Add the new zuul route in <springappname>-<profile>.properties", zuulRouteId);
				LOGGER.info(msg);
				throw new ZuulConfigNotUpdatedException(msg);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while parsing actuator refresh response", e);
		}
	}
}
