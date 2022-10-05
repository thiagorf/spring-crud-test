package com.api.crudapi.user;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.crudapi.exceptions.BadRequestException;
import com.api.crudapi.exceptions.NotFoundException;
import com.api.crudapi.security.JwtProvider;
import com.api.crudapi.user.payload.JwtResponse;
import com.api.crudapi.user.payload.RegisterUserDto;
import com.api.crudapi.user.payload.UserCredentialsDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private UserRepository userRepository;
	private ModelMapper modelMapper;

	// Spring password encoder
	private PasswordEncoder passwordEncoder;

	// Spring default authentication manager
	private AuthenticationManager authManager;

	// Create a jwt token
	private JwtProvider jwtProvider;

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

	public JwtResponse attemptLogin(UserCredentialsDto userCredentials) throws Exception {
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(userCredentials.getEmail(), userCredentials.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		UserModel user = userRepository.findByEmail(userCredentials.getEmail())
				.orElseThrow(() -> new NotFoundException());

		String jwt = jwtProvider.sign(user);

		return new JwtResponse(jwt);
	}

	public List<UserModel> getUsers() {
		return userRepository.findAll();

	}

}
