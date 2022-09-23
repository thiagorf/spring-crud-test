package com.api.crudapi.user;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import com.api.crudapi.exceptions.BadRequestException;
import com.api.crudapi.exceptions.NotFoundException;
import com.api.crudapi.security.JwtProvider;
import com.api.crudapi.user.payload.JwtResponse;
import com.api.crudapi.user.payload.LogoutResponse;
import com.api.crudapi.user.payload.RegisterUserDto;
import com.api.crudapi.user.payload.UserCredentialsDto;

@Service
public class UserService {

	@Value("${api.cookie}")
	private String cookieName;

	final UserRepository userRepository;
	final ModelMapper modelMapper;

	// Spring password encoder
	final PasswordEncoder passwordEncoder;

	// Spring default authentication manager
	final AuthenticationManager authManager;

	// Create a jwt token
	final JwtProvider jwtProvider;

	public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
			AuthenticationManager authManager, JwtProvider jwtProvider) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.authManager = authManager;
		this.jwtProvider = jwtProvider;
	}

	@Transactional
	public UserModel registerUser(RegisterUserDto registerUserDto) {
		Optional<UserModel> user = userRepository.findByEmail(registerUserDto.getEmail());

		if (user.isPresent()) {
			throw new BadRequestException("A user account with this email has already been taken");
		}

		UserModel userModel = new UserModel();
		modelMapper.map(registerUserDto, userModel);
		userModel.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

		return userRepository.save(userModel);
	}

	public JwtResponse attemptLogin(UserCredentialsDto userCredentials, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(userCredentials.getEmail(), userCredentials.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		UserModel user = userRepository.findByEmail(userCredentials.getEmail())
				.orElseThrow(() -> new NotFoundException());

		String jwt = jwtProvider.sign(user);

		Cookie cookie = new Cookie(cookieName, jwt);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(60);

		response.addCookie(cookie);

		return new JwtResponse(jwt);
	}

	public LogoutResponse logout(HttpServletRequest request, HttpServletResponse response) {

		Cookie cookie = WebUtils.getCookie(request, cookieName);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);

		response.addCookie(cookie);

		return new LogoutResponse("You have successfully logged out!");

	}

	public List<UserModel> getUsers() {
		return userRepository.findAll();

	}

}
