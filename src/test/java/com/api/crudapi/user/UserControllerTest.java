package com.api.crudapi.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.api.crudapi.user.payload.RegisterUserDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = UserController.class)
public class UserControllerTest {

	@MockBean
	private UserService userService;
	@Autowired
	private MockMvc mockMvc;

	@Test
	void createUser() throws Exception {

		when(userService.registerUser(any(RegisterUserDto.class)))
				.thenReturn(new UserModel(UUID.randomUUID(), "test", "test@gmail.com", "test1234", new HashSet<>()));

		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.content("{\"name\": \"test\", \"email\": \"test@gmail.com\", \"password\": \"test1234\"}")
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated());
	}
}
