package com.api.crudapi.user.payload;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {

	@NotBlank
	private String name;

	@NotBlank
	private String email;

	@NotBlank
	private String password;

}
