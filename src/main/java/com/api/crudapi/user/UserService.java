package com.api.crudapi.user;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.crudapi.auth.JwtUtil;
import com.api.crudapi.exceptions.BadRequestException;
import com.api.crudapi.exceptions.NotFoundException;
import com.api.crudapi.user.payload.JwtResponse;
import com.api.crudapi.user.payload.RegisterUserDto;
import com.api.crudapi.user.payload.UserCredentialsDto;

@Service
public class UserService implements UserDetailsService {

	final UserRepository userRepository;
	final ModelMapper modelMapper;

	// Spring password encoder
	final PasswordEncoder passwordEncoder;

	// Spring default authentication manager
	final AuthenticationManager authManager;

	// Create a jwt token
	final JwtUtil jwtUtil;

	public UserService(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder,
			AuthenticationManager authManager, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.authManager = authManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	// The funny part is: username can be anything like a email or a Id
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		var userDetails = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException());

		// It's ok, i think
		return (UserDetails) userDetails;
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

	public JwtResponse attemptLogin(UserCredentialsDto userCredentials) throws Exception {
		//
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(userCredentials.getEmail(), userCredentials.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		final UserDetails user = loadUserByUsername(userCredentials.getEmail());
		final String jwt = jwtUtil.generateToken(user);

		return new JwtResponse(jwt);
	}

	public List<UserModel> getUsers() {
		return userRepository.findAll();

	}

}
