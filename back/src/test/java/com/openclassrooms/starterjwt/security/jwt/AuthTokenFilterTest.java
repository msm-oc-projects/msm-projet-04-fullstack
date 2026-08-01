package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    private AuthTokenFilter filter;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        filter = new AuthTokenFilter();
        ReflectionTestUtils.setField(filter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(filter, "userDetailsService", userDetailsService);
        response = new MockHttpServletResponse();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void ignoresRequestWithoutBearerToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void authenticatesRequestWithValidToken() throws Exception {
        MockHttpServletRequest request = requestWithToken("valid-token");
        UserDetailsImpl details = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .password("password")
                .admin(false)
                .build();
        when(jwtUtils.validateJwtToken("valid-token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid-token")).thenReturn(details.getUsername());
        when(userDetailsService.loadUserByUsername(details.getUsername())).thenReturn(details);

        filter.doFilterInternal(request, response, filterChain);

        assertEquals(details, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void ignoresInvalidToken() throws Exception {
        MockHttpServletRequest request = requestWithToken("invalid-token");
        when(jwtUtils.validateJwtToken("invalid-token")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void continuesChainWhenTokenProcessingFails() throws Exception {
        MockHttpServletRequest request = requestWithToken("broken-token");
        when(jwtUtils.validateJwtToken("broken-token")).thenThrow(new IllegalStateException("failure"));

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    private MockHttpServletRequest requestWithToken(String token) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        return request;
    }
}
