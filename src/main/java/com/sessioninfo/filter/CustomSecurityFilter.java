package com.sessioninfo.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author Sathwik on 21/june/2020
 */
@Slf4j
@Component
public class CustomSecurityFilter extends OncePerRequestFilter {

    private static final String ESCAPE_CHARACTERS = "<,>,;,{,},%3C,%3E,&gt;,&lt;";

    private static final String[] ESCAPE_CHARACTERS_LIST = ESCAPE_CHARACTERS.split(",");

    /**
     * Checks if there is any black listed special characters are present in the request uri
     * which will try to avoid any attack
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {
        String requestURI = httpServletRequest.getRequestURI();
        if (StringUtils.indexOfAny(requestURI, ESCAPE_CHARACTERS_LIST) >= 0) {
            httpServletResponse.sendError(BAD_REQUEST.value(), "Bad request");
            log.error("The request requestUri is invalid : {} {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
