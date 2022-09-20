package com.api.crudapi.user.payload;

import javax.validation.constraints.NotBlank;

public class JwtResponse {

	@NotBlank
	private String jwt;

	public JwtResponse(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

}
