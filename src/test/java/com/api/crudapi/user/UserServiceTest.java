package com.api.crudapi.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.crudapi.exceptions.BadRequestException;
import com.api.crudapi.exceptions.NotFoundException;
import com.api.crudapi.security.JwtProvider;
import com.api.crudapi.user.payload.JwtResponse;
import com.api.crudapi.user.payload.RegisterUserDto;
import com.api.crudapi.user.payload.UserCredentialsDto;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AuthenticationManager auth;
	@Mock
	private JwtProvider jwtProvider;
	@InjectMocks
	private UserService userService;

	@Test
	@DisplayName("Should be able to register an user")
	void registerUser() {
		UserModel user = new UserModel(UUID.randomUUID(), "test", "test@gmail.com", "test1234", new HashSet<>());

		RegisterUserDto credentials = new RegisterUserDto();
		credentials.setEmail("test@gmail.com");
		credentials.setName("test");
		credentials.setPassword("test1234");

		when(userRepository.findByEmail(credentials.getEmail())).thenReturn(Optional.empty());
		when(userRepository.save(any(UserModel.class))).thenReturn(user);

		var sut = userService.registerUser(credentials);

		assertThat(sut).hasNoNullFieldsOrPropertiesExcept("vehicles");
	}

	@Test
	@DisplayName("Should not be able to register an user with duplicated email")
	void registerUserBadRequestException() {
		UserModel user = new UserModel(UUID.randomUUID(), "test", "test@gmail.com", "test1234", new HashSet<>());
		RegisterUserDto credentials = new RegisterUserDto();
		credentials.setEmail("test@gmail.com");
		credentials.setName("test");
		credentials.setPassword("test1234");
		when(userRepository.findByEmail(credentials.getEmail())).thenReturn(Optional.of(user));

		assertThrows(BadRequestException.class, () -> userService.registerUser(credentials));
	}

	@Test
	@DisplayName("Should be able to return all users")
	void getUsers() {
		when(userRepository.findAll()).thenReturn(new ArrayList<UserModel>());

		List<UserModel> sut = userService.getUsers();

		assertThat(sut).asList();
	}

	@Test
	@DisplayName("Should be able to authenticate a user")
	void attemptLoggin() throws Exception {
		UserModel user = new UserModel(UUID.randomUUID(), "test", "test@gmail.com", "test1234", new HashSet<>());
		UserCredentialsDto credentials = new UserCredentialsDto();
		credentials.setEmail("test@gmail.com");
		credentials.setPassword("test1234");

		String jwtString = "jwt token";

		when(userRepository.findByEmail(credentials.getEmail())).thenReturn(Optional.of(user));
		when(jwtProvider.sign(any(UserModel.class))).thenReturn(jwtString);

		JwtResponse sut = userService.attemptLogin(credentials);

		assertThat(sut.getJwt()).contains(jwtString);
	}

	@Test
	@DisplayName("Should not be able to proceed with authentication manager with bad credentials")
	void shouldReturnExceptionWithBadCredentials() throws Exception {
		UserCredentialsDto credentials = new UserCredentialsDto();
		credentials.setEmail("test@gmail.com");
		credentials.setPassword("test1234");

		when(auth.authenticate(any(Authentication.class))).thenThrow(BadCredentialsException.class);

		assertThrows(Exception.class, () -> userService.attemptLogin(credentials));
	}

	// TODO authentication manager already checks if the user exists, remove this
	// check?
	@Test
	@DisplayName("Should not be able to authentica with an invalid user")
	void shouldReturnNotFoundExceptionWithInvalidUser() {
		UserCredentialsDto credentials = new UserCredentialsDto();
		credentials.setEmail("test@gmail.com");
		credentials.setPassword("test1234");
		when(userRepository.findByEmail("test@gmail.com")).thenThrow(NotFoundException.class);

		assertThrows(NotFoundException.class, () -> userService.attemptLogin(credentials));
	}
}
