package com.api.crudapi.parkingSpot;

import static com.api.crudapi.util.JsonUtil.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
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

import com.api.crudapi.parkingspot.ParkingSpotController;
import com.api.crudapi.parkingspot.ParkingSpotDto;
import com.api.crudapi.parkingspot.ParkingSpotModel;
import com.api.crudapi.parkingspot.ParkingSpotService;
import com.api.crudapi.parkingspot.payload.ParkingSpotCarsResponse;
import com.api.crudapi.vehicle.VehicleModel;

@ExtendWith(SpringExtension.class)
@WebMvcTest
//Public routes, should never be blocked by spring security
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = { ParkingSpotController.class })
public class ParkingSpotControllerTest {

	@MockBean
	private ParkingSpotService parkingSpotService;

	@Autowired
	private MockMvc mockMvc;

	private ParkingSpotModel parkingSpot;
	private ParkingSpotDto dto;
	private VehicleModel vehicle;

	@BeforeEach
	void setupTest() {
		parkingSpot = ParkingSpotModel.builder().id(UUID.randomUUID()).apartment("apartment A").block("block A")
				.parkingSpotNumber("test").build();
		dto = ParkingSpotDto.builder().apartment("apartment A").block("block A").parkingSpotNumber("test").build();
		vehicle = VehicleModel.builder().id(UUID.randomUUID()).brand("test brand").build();
	}

	@Test
	void saveParkingSpot() throws Exception {

		when(parkingSpotService.addParkingSpot(any(ParkingSpotDto.class))).thenReturn(parkingSpot);

		mockMvc.perform(post("/parking-spot").contentType(MediaType.APPLICATION_JSON).content(asJsonString(dto))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(redirectedUrlPattern("**/parking-spot/*"));

	}

	@Test
	void updateParkingSpot() throws Exception {
		ParkingSpotModel updatedParkingSpot = ParkingSpotModel.builder().id(parkingSpot.getId())
				.apartment("apartment B").block("blockA").parkingSpotNumber("test").build();

		ParkingSpotDto updatedDto = ParkingSpotDto.builder().apartment("apartment B").block("block A")
				.parkingSpotNumber("test").build();

		when(parkingSpotService.updateParkingSpot(any(UUID.class), any(ParkingSpotDto.class)))
				.thenReturn(updatedParkingSpot);

		mockMvc.perform(put("/parking-spot/" + parkingSpot.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(updatedDto)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.apartment").value(updatedParkingSpot.getApartment()));

	}

	@Test
	void getOneParkingSpot() throws Exception {

		parkingSpot.setVehicles(vehicle);
		ParkingSpotCarsResponse response = ParkingSpotCarsResponse.builder().id(parkingSpot.getId())
				.apartment(parkingSpot.getApartment()).block(parkingSpot.getBlock())
				.parkingSpotNumber(parkingSpot.getParkingSpotNumber()).vehicles(parkingSpot.getVehicles())
				.spotsQuantity(parkingSpot.getSpotsQuantity()).build();

		when(parkingSpotService.findOneParkingSpot(parkingSpot.getId())).thenReturn(response);

		mockMvc.perform(get("/parking-spot/" + parkingSpot.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.vehicles").isNotEmpty());
	}

	@Test
	void parkVehicle() throws Exception {

		parkingSpot.setVehicles(vehicle);
		when(parkingSpotService.parkVehicle(parkingSpot.getId(), vehicle.getId())).thenReturn(parkingSpot);

		mockMvc.perform(put("/parking-spot/" + parkingSpot.getId() + "/vehicles/" + vehicle.getId())
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.vehicles").isNotEmpty());
	}

	@Test
	void leaveParkingSpot() throws Exception {

		// I don't have any idea how to test this controller
		parkingSpot.setVehicles(vehicle);

		assertThat(parkingSpot.getVehicles()).hasAtLeastOneElementOfType(VehicleModel.class);

		parkingSpot.removeVehicles(vehicle);

		when(parkingSpotService.leaveParkingSpot(parkingSpot.getId(), vehicle.getId())).thenReturn(parkingSpot);

		mockMvc.perform(put("/parking-spot/" + parkingSpot.getId() + "/vehicles/" + vehicle.getId() + "/leave")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.vehicles").isEmpty());

		assertThat(parkingSpot.getVehicles()).isEmpty();
	}

}
