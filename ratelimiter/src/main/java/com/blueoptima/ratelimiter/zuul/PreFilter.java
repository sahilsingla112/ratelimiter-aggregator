package com.blueoptima.ratelimiter.zuul;

import javax.servlet.http.HttpServletRequest;

import com.blueoptima.ratelimiter.model.ApiInfo;
import com.blueoptima.ratelimiter.service.RateLimitService;
import com.blueoptima.ratelimiter.service.RateLimitServiceLocator;
import com.blueoptima.ratelimiter.service.UserApiConfigService;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */

/**
 * Checks the API is allowed or not.
 */
public class PreFilter extends ZuulFilter {

	private static final Logger LOG = LoggerFactory.getLogger(PreFilter.class);
	private static final String REJECT_MESSAGE = "API limit exceeded! Request is rejected";

	@Autowired
	private UserApiConfigService userApiConfigService;

	@Autowired
	private RateLimitServiceLocator serviceLocator;

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return FilterConstants.SEND_RESPONSE_FILTER_ORDER;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		String userid = ctx.getRequest().getHeader("userid");

		if (StringUtils.isEmpty(userid)){
			String errorMessage = "Requests without userid are not allowed. Add a header of 'userid' in your request";
			LOG.error(errorMessage);
			ctx.setResponseBody(errorMessage);
			ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
			ctx.setSendZuulResponse(false);
			return null;
		}

		String fullUrl = request.getRequestURL().toString();
		LOG.info(String.format("%s request to %s", request.getMethod(), fullUrl));

		final String[] strings = fullUrl.split("//");

		if (strings.length > 1) {
			String urlWithoutProtocol = strings[1];

			final String[] split = urlWithoutProtocol.split("/");

			if (split.length > 2) {
				final String[] downStreamUriFields = Arrays.copyOfRange(split, 2, split.length);

				String downStreamUri = "/" + String.join("/", downStreamUriFields);

				ApiInfo apiInfo = userApiConfigService.getApiInfo(downStreamUri);

				if (apiInfo == null){
					LOG.error(String.format("API %s is not registered with the Rate limiter. Therefore, rate limiting feature can not be applied to this. "
							+ "Refer documentation for how to register an API with the Gateway.", downStreamUri));
					return null;
				}

				final Integer maxRateLimitAllowed = userApiConfigService.getRateLimit(userid, downStreamUri);


				final RateLimitService rateLimitService = serviceLocator.getRateLimitService(apiInfo.getRateLimitStrategy());
				final boolean allowRequest = rateLimitService.allowRequest(userid, apiInfo, maxRateLimitAllowed);

				if (!allowRequest) {
					LOG.warn(String.format("%s for %s", REJECT_MESSAGE, downStreamUri));
					ctx.setResponseBody(REJECT_MESSAGE);
					ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
					ctx.setSendZuulResponse(false);
					return null;
				}
			}
		}

		return null;
	}
}