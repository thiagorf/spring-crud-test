package com.api.crudapi.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.crudapi.exceptions.BadRequestException;
import com.api.crudapi.security.JwtProvider;
import com.api.crudapi.user.payload.RegisterUserDto;

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
}
