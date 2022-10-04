package com.api.crudapi.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.api.crudapi.exceptions.NotFoundException;
import com.api.crudapi.security.auth.AuthUserDetails;
import com.api.crudapi.security.auth.AuthUserDetailsService;
import com.api.crudapi.security.auth.RequestCookie;
import com.api.crudapi.user.UserModel;

@ExtendWith(MockitoExtension.class)
public class JwtTokenFilterTest {
	@InjectMocks
	private JwtTokenFilter jwtTokenFilter;

	@Mock
	private AuthUserDetailsService userDetailsService;

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private RequestCookie requestCookie;

	private String cookieName = "jwt-cookie";

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private FilterChain filterChain;

	@AfterEach
	void cleanContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void doFilterInternal() throws ServletException, IOException {

		ReflectionTestUtils.setField(jwtTokenFilter, "cookieName", cookieName);

		// The Security Context is stored in Thread Local, I think is ok to test like
		// this
		// Since this is the only way to check if the filter is doing its job
		SecurityContext authenticationContext = SecurityContextHolder.getContext();

		Cookie cookie = new Cookie(cookieName, "jwt");

		AuthUserDetails userDetails = new AuthUserDetails(
				new UserModel(UUID.randomUUID(), "test", "test@gmail.com", "test1234", new HashSet<>()));

		doReturn(cookie).when(requestCookie).getCookies(any(HttpServletRequest.class), eq(cookieName));
		when(jwtProvider.extractEmail(anyString())).thenReturn("test@gmail.com");
		when(userDetailsService.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);

		assertThat(authenticationContext.getAuthentication()).isNull();

		jwtTokenFilter.doFilterInternal(request, response, filterChain);

		assertThat(authenticationContext.getAuthentication()).isNotNull();
		assertThat(authenticationContext.getAuthentication().isAuthenticated()).isTrue();
	}

	@Test
	void doFilterChainUserNotFoundException() throws ServletException, IOException {
		ReflectionTestUtils.setField(jwtTokenFilter, "cookieName", cookieName);

		Cookie cookie = new Cookie(cookieName, "jwt");

		doReturn(cookie).when(requestCookie).getCookies(any(HttpServletRequest.class), eq(cookieName));
		when(jwtProvider.extractEmail(anyString())).thenReturn("test@gmail.com");
		when(userDetailsService.loadUserByUsername(anyString())).thenThrow(NotFoundException.class);

		assertThrows(NotFoundException.class, () -> jwtTokenFilter.doFilterInternal(request, response, filterChain));
	}
}
