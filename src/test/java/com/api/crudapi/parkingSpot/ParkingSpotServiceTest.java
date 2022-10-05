package com.api.crudapi.parkingSpot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.api.crudapi.exceptions.BadRequestException;
import com.api.crudapi.exceptions.NotFoundException;
import com.api.crudapi.parkingspot.ParkingSpotDto;
import com.api.crudapi.parkingspot.ParkingSpotModel;
import com.api.crudapi.parkingspot.ParkingSpotRepository;
import com.api.crudapi.parkingspot.ParkingSpotService;
import com.api.crudapi.vehicle.VehicleModel;
import com.api.crudapi.vehicle.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotServiceTest {
	@InjectMocks
	private ParkingSpotService parkingSpotService;

	@Mock
	private ParkingSpotRepository parkingSpotRepository;

	@Mock
	private VehicleRepository vehicleRepository;

	@Mock
	private ModelMapper modelMapper;

	private ParkingSpotModel parkingSpot;
	private ParkingSpotDto dto;
	private VehicleModel vehicle;

	@BeforeEach
	void setupTest() {
		parkingSpot = ParkingSpotModel.builder().apartment("apartment A").block("block A").id(UUID.randomUUID())
				.parkingSpotNumber("test").build();
		dto = ParkingSpotDto.builder().apartment("apartment A").block("block A").parkingSpotNumber("test").build();
		vehicle = VehicleModel.builder().id(UUID.randomUUID()).brand("test").build();
	}

	@Test
	void createParkingSpot() {

		when(parkingSpotRepository.save(any(ParkingSpotModel.class))).thenReturn(parkingSpot);

		ParkingSpotModel sut = parkingSpotService.addParkingSpot(dto);

		assertThat(sut.getId()).isNotNull();
		assertThat(sut).hasNoNullFieldsOrProperties();
	}

	@Test
	void updateParkingSpot() {

		ParkingSpotDto updatedDto = ParkingSpotDto.builder().apartment("apartment A").block("block B")
				.parkingSpotNumber("test").build();
		ParkingSpotModel updatedParkingSpot = ParkingSpotModel.builder().id(UUID.randomUUID()).apartment("apartment A")
				.block("block B").parkingSpotNumber("teste").build();

		updatedParkingSpot.setBlock("block B");
		updatedDto.setBlock("block B");

		when(parkingSpotRepository.findById(parkingSpot.getId())).thenReturn(Optional.of(parkingSpot));
		when(parkingSpotRepository.save(any(ParkingSpotModel.class))).thenReturn(updatedParkingSpot);

		ParkingSpotModel sut = parkingSpotService.updateParkingSpot(parkingSpot.getId(), updatedDto);

		assertThat(sut).isNotEqualTo(parkingSpot);
	}

	@Test
	void updateParkingSpotNotFoundException() {

		when(parkingSpotRepository.findById(parkingSpot.getId())).thenThrow(NotFoundException.class);

		assertThrows(NotFoundException.class, () -> parkingSpotService.updateParkingSpot(parkingSpot.getId(), dto));
	}

	@Test
	void parkVehicle() {

		when(parkingSpotRepository.findById(parkingSpot.getId())).thenReturn(Optional.of(parkingSpot));
		when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
		parkingSpot.setVehicles(vehicle);
		when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

		ParkingSpotModel sut = parkingSpotService.parkVehicle(parkingSpot.getId(), vehicle.getId());

		assertThat(sut.getVehicles()).hasAtLeastOneElementOfType(VehicleModel.class);
	}

	@Test
	void leaveParkingSpot() {

		Set<VehicleModel> vehicles = new HashSet<VehicleModel>();
		vehicles.add(vehicle);

		ParkingSpotModel parkingSpotModel = ParkingSpotModel.builder().id(UUID.randomUUID()).block("block A")
				.apartment("apartment A").parkingSpotNumber("test").vehicles(vehicles).build();

		when(parkingSpotRepository.findById(parkingSpotModel.getId())).thenReturn(Optional.of(parkingSpotModel));
		when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
		when(parkingSpotRepository.save(any(ParkingSpotModel.class))).thenReturn(parkingSpot);

		assertThat(parkingSpotModel.getVehicles()).hasOnlyElementsOfType(VehicleModel.class);

		ParkingSpotModel sut = parkingSpotService.leaveParkingSpot(parkingSpotModel.getId(), vehicle.getId());

		assertThat(sut.getVehicles()).doesNotContain(vehicle);

	}

	@Test
	void leavParkingSpotBadRequestException() {

		when(parkingSpotRepository.findById(parkingSpot.getId())).thenReturn(Optional.of(parkingSpot));
		when(vehicleRepository.findById(vehicle.getId())).thenThrow(BadRequestException.class);

		assertThrows(BadRequestException.class,
				() -> parkingSpotService.leaveParkingSpot(parkingSpot.getId(), vehicle.getId()));
	}
}
