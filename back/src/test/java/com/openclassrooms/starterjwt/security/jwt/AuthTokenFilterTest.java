package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Test
    public void testDoFilterInternal() throws ServletException, IOException {
        // Create mock objects for request, response and filter chain
        HttpServletRequest request = mockRequest("Bearer jwt");
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Stub the methods of jwtUtils and userDetailsService
        when(jwtUtils.validateJwtToken("jwt")).thenReturn(true);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(UserDetailsImpl.builder().build());

        // Call the method under test and verify the interactions
        authTokenFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    // Helper method to create a mock request with a given authorization header
    private HttpServletRequest mockRequest(String authorization) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(authorization);
        return request;
    }
}
