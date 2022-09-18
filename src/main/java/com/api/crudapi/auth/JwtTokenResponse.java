package com.api.crudapi.auth;

public class JwtTokenResponse {

	private final String jwt;

	public JwtTokenResponse(String jwt) {
		super();
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}
	
	
}
