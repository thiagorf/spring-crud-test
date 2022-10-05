package com.api.crudapi.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.crudapi.security.auth.AuthUserDetailsService;
import com.api.crudapi.security.auth.RequestCookie;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	@Value("${api.cookie}")
	private String cookieName;

	@Autowired
	private AuthUserDetailsService userDetailsService;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private RequestCookie requestCookie;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Cookie cookie = WebUtils.getCookie(request, cookieName);

		Cookie cookie = requestCookie.getCookies(request, cookieName);

		String token = null;
		String email = null;

		if (cookie != null) {
			token = cookie.getValue();
			email = jwtProvider.extractEmail(token);
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			// Todo extractEmail already verify the token
			var isTokenValid = jwtProvider.verify(token);

			if (!(isTokenValid instanceof Exception)) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
						new ArrayList<>());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}

		filterChain.doFilter(request, response);
	}

}
