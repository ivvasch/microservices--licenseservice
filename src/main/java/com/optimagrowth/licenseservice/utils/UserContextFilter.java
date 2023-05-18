package com.optimagrowth.licenseservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class UserContextFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        UserContextHolder.getContext().setCorrelationId(UserContext.CORRELATION_ID);
        UserContextHolder.getContext().setUserId(UserContext.USER_ID);
        UserContextHolder.getContext().setAuthToken(UserContext.AUTH_TOKEN);
        UserContextHolder.getContext().setOrganizationId(UserContext.ORGANIZATION_ID);
        logger.debug("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        chain.doFilter(httpServletRequest, response);
    }
}
