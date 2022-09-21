package com.api.crudapi.user;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.crudapi.user.payload.JwtResponse;
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
	public ResponseEntity<UserModel> createUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
		UserModel createdUser = userService.registerUser(registerUserDto);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(createdUser.getId()).toUri();

		return ResponseEntity.created(location).body(createdUser);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody @Valid UserCredentialsDto userCredentials) throws Exception {
		JwtResponse jwt = userService.attemptLogin(userCredentials);

		return ResponseEntity.ok().body(jwt);
	}

	@GetMapping
	public ResponseEntity<List<UserModel>> getAllUsers() {
		List<UserModel> users = userService.getUsers();

		return ResponseEntity.ok().body(users);
	}
}
