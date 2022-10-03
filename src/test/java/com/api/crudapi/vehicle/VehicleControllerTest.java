package com.api.crudapi.vehicle;

import static com.api.crudapi.util.JsonUtil.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.api.crudapi.security.JwtProvider;
import com.api.crudapi.security.SecurityConfig;
import com.api.crudapi.user.UserModel;
import com.api.crudapi.user.UserRepository;
import com.api.crudapi.vehicle.payload.VehicleDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
// Given the class necessary for the testing context, is required to Mock each of its dependencies
// VehicleController -> VehicleService, SecurityConfig -> JwtProvider
@ContextConfiguration(classes = { SecurityConfig.class, VehicleController.class })
public class VehicleControllerTest {

	@Value("${api.cookie}")
	private String cookieName;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private VehicleService vehicleService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private JwtProvider jwtProvider;

	private MockMvc mockMvc;

	private final String email = "test@gmail.com";

	@BeforeEach
	void setupTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	@WithMockUser(email)
	void getVehicles() throws Exception {
		mockMvc.perform(get("/vehicles").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(email)
	void createVehicle() throws Exception {
		UserModel user = new UserModel(UUID.randomUUID(), "test", email, "test1234", new HashSet<>());

		VehicleModel vehicle = new VehicleModel(UUID.randomUUID(), "test", null, user);

		when(vehicleService.save(any(VehicleDto.class), eq(email))).thenReturn(vehicle);

		mockMvc.perform(post("/vehicles").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(new VehicleDto("test"))).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.brand").value("test"))
				.andExpect(redirectedUrlPattern("**/vehicles/*"));
	}

	@Test
	@WithMockUser(email)
	void updateVehicle() throws Exception {
		UserModel user = new UserModel(UUID.randomUUID(), "test", email, "test1234", new HashSet<>());
		String updatedBrand = "updated-brand";

		VehicleModel vehicle = new VehicleModel(UUID.randomUUID(), "test", null, user);
		VehicleModel updatedVehicle = new VehicleModel(UUID.randomUUID(), updatedBrand, null, user);

		when(vehicleService.updateVehicle(any(UUID.class), any(VehicleDto.class))).thenReturn(updatedVehicle);

		var response = mockMvc
				.perform(put("/vehicles/" + vehicle.getId().toString()).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(new VehicleDto(updatedBrand))).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.brand").value(updatedBrand)).andReturn();

		assertThat(response).isNotSameAs(vehicle);
	}
}
