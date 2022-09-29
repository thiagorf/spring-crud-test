package com.api.crudapi.user.payload;

import javax.validation.constraints.NotBlank;

public class RegisterUserDto {

	@NotBlank
	private String name;

	@NotBlank
	private String email;

	@NotBlank
	private String password;

	public RegisterUserDto() {
	}

	public RegisterUserDto(@NotBlank String name, @NotBlank String email, @NotBlank String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
