package com.api.crudapi.user;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.crudapi.user.payload.JwtResponse;
import com.api.crudapi.user.payload.LogoutResponse;
import com.api.crudapi.user.payload.RegisterUserDto;
import com.api.crudapi.user.payload.UserCredentialsDto;

@RestController
@RequestMapping("/users")
public class UserController {

	final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<UserModel> createUser(@RequestBody @Valid RegisterUserDto registerUserDto,
			UriComponentsBuilder uriComponent) {
		UserModel createdUser = userService.registerUser(registerUserDto);

		URI location = uriComponent.path("/users/{id}").build(createdUser.getId());

		return ResponseEntity.created(location).body(createdUser);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody @Valid UserCredentialsDto userCredentials,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		JwtResponse jwt = userService.attemptLogin(userCredentials, request, response);

		return ResponseEntity.ok().body(jwt);
	}

	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(HttpServletRequest request, HttpServletResponse response) {
		// TODO cookie not found error
		LogoutResponse message = userService.logout(request, response);

		return ResponseEntity.ok().body(message);
	}

	@GetMapping
	public ResponseEntity<List<UserModel>> getAllUsers() {
		List<UserModel> users = userService.getUsers();

		return ResponseEntity.ok().body(users);
	}
}
