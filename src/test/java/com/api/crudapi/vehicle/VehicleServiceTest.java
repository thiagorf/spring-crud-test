package com.api.crudapi.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.api.crudapi.exceptions.NotFoundException;
import com.api.crudapi.user.UserModel;
import com.api.crudapi.user.UserRepository;
import com.api.crudapi.vehicle.payload.VehicleDto;
import com.api.crudapi.vehicle.payload.VehicleResponse;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

	@Mock
	private VehicleRepository vehicleRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ModelMapper modelMapper;
	@InjectMocks
	private VehicleService vehicleService;

	private VehicleModel vehicle;

	@BeforeEach
	void setuptest() {
		vehicle = new VehicleModel(UUID.randomUUID(), "test-brand", null, null);
	}

	@Test
	void save() {
		String email = "test@gmail.com";
		UserModel user = new UserModel(UUID.randomUUID(), "test", email, "test1234", new HashSet<>());
		VehicleModel vehicleWithUser = new VehicleModel(UUID.randomUUID(), "test-brand", null, user);

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(vehicleRepository.save(any(VehicleModel.class))).thenReturn(vehicleWithUser);

		VehicleModel sut = vehicleService.save(new VehicleDto(vehicle.getBrand()), email);

		assertThat(sut).hasNoNullFieldsOrPropertiesExcept("parkingSpot");
		assertThat(sut.getUser()).isInstanceOf(UserModel.class);
	}

	@Test
	@DisplayName("Should not be able to create a vehicle with an invalid user")
	void createVehicleUserNotFoundException() {
		String email = "test@gmail.com";
		when(userRepository.findByEmail(email)).thenThrow(NotFoundException.class);
		assertThrows(NotFoundException.class, () -> vehicleService.save(new VehicleDto("test-brand"), email));
	}

	@Test
	void getAllVehicles() {
		when(vehicleRepository.findAll()).thenReturn(new ArrayList<VehicleModel>());

		List<VehicleResponse> sut = vehicleService.getAllVehicles();

		assertThat(sut).asList();
	}

	@Test
	void getOneVehicle() {

		when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));

		VehicleModel sut = vehicleService.getOneVehicle(vehicle.getId());

		assertThat(sut).hasNoNullFieldsOrPropertiesExcept("parkingSpot", "user");
	}

	@Test
	@DisplayName("Should be able to update a vehicle")
	void updateVehicle() {
		VehicleModel updatedVehicle = new VehicleModel(vehicle.getId(), "updated-test-brand", null, null);
		when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
		when(vehicleRepository.save(any(VehicleModel.class))).thenReturn(updatedVehicle);

		VehicleModel sut = vehicleService.updateVehicle(vehicle.getId(), new VehicleDto(updatedVehicle.getBrand()));

		assertThat(sut).isEqualTo(updatedVehicle);
		assertThat(sut).isNotEqualTo(vehicle);
	}

	@Test
	@DisplayName("Should be able to delete a vehicle")
	void deleteVehicle() {
		when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));

		assertThatNoException().isThrownBy(() -> vehicleService.deleteVehicle(vehicle.getId()));
	}
}
