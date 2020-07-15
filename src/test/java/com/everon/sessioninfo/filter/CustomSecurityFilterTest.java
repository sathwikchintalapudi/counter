package com.everon.sessioninfo.filter;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomSecurityFilterTest {

    @Test
    public void testDoFilterInternal() throws Exception {
        CustomSecurityFilter customSecurityFilter = new CustomSecurityFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/games");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        customSecurityFilter.doFilterInternal(request, response, filterChain);
        assertNull(response.getErrorMessage());
    }

    @Test
    public void testDoFilterInternal_whenInvalidRequest() throws Exception {
        CustomSecurityFilter customSecurityFilter = new CustomSecurityFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/<games");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        customSecurityFilter.doFilterInternal(request, response, filterChain);
        assertNotNull(response.getErrorMessage());
        assertEquals("Bad request", response.getErrorMessage());
    }

    @Test
    public void testDoFilterInternal_whenInvalidPathInfo() throws Exception {
        CustomSecurityFilter customSecurityFilter = new CustomSecurityFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/games/123456789</");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        customSecurityFilter.doFilterInternal(request, response, filterChain);
        assertNotNull(response.getErrorMessage());
        assertEquals("Bad request", response.getErrorMessage());
    }
}
