package com.api.crudapi.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	private AuthenticationManager authenticationManager;
	private AuthUserDetailsService userDetailsService;
	private JwtUtil jwtTokenUtil;
	
	public AuthController(AuthenticationManager authenticationManager, AuthUserDetailsService userDetailsService, JwtUtil jwtTokenUtil) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtTokenResponse> login(@RequestBody UserCredentialsDto userCredentials) throws Exception {
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword()));
		
		
		} catch(BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userCredentials.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok().body(new JwtTokenResponse(jwt));
	}
}
