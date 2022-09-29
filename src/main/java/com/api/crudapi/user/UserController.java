package com.api.crudapi.user;

import java.net.URI;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import com.api.crudapi.user.payload.JwtResponse;
import com.api.crudapi.user.payload.LogoutResponse;
import com.api.crudapi.user.payload.RegisterUserDto;
import com.api.crudapi.user.payload.UserCredentialsDto;

@RestController
@RequestMapping("/users")
public class UserController {

	@Value("${api.cookie}")
	private String cookieName;

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
		JwtResponse jwt = userService.attemptLogin(userCredentials);

		Cookie cookie = new Cookie(cookieName, jwt.getJwt());
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(60);

		response.addCookie(cookie);

		return ResponseEntity.ok().body(jwt);
	}

	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(HttpServletRequest request, HttpServletResponse response) {
		// TODO cookie not found error
		// TODO remove Authentication object from ThreadLocal

		Cookie cookie = WebUtils.getCookie(request, cookieName);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);

		response.addCookie(cookie);

		return ResponseEntity.ok().body(new LogoutResponse("You have successfully logged out!"));
	}

	@GetMapping
	public ResponseEntity<List<UserModel>> getAllUsers() {
		List<UserModel> users = userService.getUsers();

		return ResponseEntity.ok().body(users);
	}
}
