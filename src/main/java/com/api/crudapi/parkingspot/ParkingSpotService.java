package com.api.crudapi.parkingspot;

import java.util.UUID;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.crudapi.exceptions.BadRequestException;
import com.api.crudapi.exceptions.NotFoundException;
import com.api.crudapi.parkingspot.payload.ParkingSpotCarsResponse;
import com.api.crudapi.parkingspot.payload.ParkingSpotResponse;
import com.api.crudapi.vehicle.VehicleModel;
import com.api.crudapi.vehicle.VehicleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParkingSpotService {

	private ParkingSpotRepository parkingSpotRepository;
	private VehicleRepository vehicleRepository;
	private ModelMapper modelMapper;

	@Transactional
	public ParkingSpotModel addParkingSpot(ParkingSpotDto parkingSpotDto) {
		var parkingSpotModel = new ParkingSpotModel();
		modelMapper.map(parkingSpotDto, parkingSpotModel);

		return parkingSpotRepository.save(parkingSpotModel);
	}

	public Page<ParkingSpotResponse> findAllParkingSpots(Pageable pageable) {

		return parkingSpotRepository.findAll(pageable).map(ParkingSpotResponse::new);
	}

	public ParkingSpotCarsResponse findOneParkingSpot(UUID id) {
		ParkingSpotModel parkingSpot = parkingSpotRepository.findById(id).orElseThrow(NotFoundException::new);
		// 1 way
		// var parkingSpotResponse = new ParkingSpotCarsResponse(parkingSpot.get());

		// 2 way
		// var parkingSpotResponse = new ParkingSpotCarsResponse();
		// BeanUtils.copyProperties(parkingSpot.get(), parkingSpotResponse);

		// 3 way
		var parkingSpotResponse = modelMapper.map(parkingSpot, ParkingSpotCarsResponse.class);

		return parkingSpotResponse;
	}

	@Transactional
	public void deleteParkingSpot(UUID id) {
		ParkingSpotModel parkingSpot = parkingSpotRepository.findById(id).orElseThrow(NotFoundException::new);

		parkingSpotRepository.delete(parkingSpot);
	}

	public ParkingSpotModel updateParkingSpot(UUID id, ParkingSpotDto parkingSpotDto) {
		ParkingSpotModel parkingSpot = parkingSpotRepository.findById(id).orElseThrow(NotFoundException::new);

		modelMapper.map(parkingSpotDto, parkingSpot);

		return parkingSpotRepository.save(parkingSpot);
	}

	public ParkingSpotModel parkVehicle(UUID parkingSpotId, UUID vehicleId) {
		ParkingSpotModel parkingSpot = parkingSpotRepository.findById(parkingSpotId)
				.orElseThrow(NotFoundException::new);

		VehicleModel vehicle = vehicleRepository.findById(vehicleId).orElseThrow(NotFoundException::new);

		vehicle.setParkingSpot(parkingSpot);
		vehicleRepository.save(vehicle);

		return parkingSpot;

	}

	public ParkingSpotModel leaveParkingSpot(UUID parkingSpotId, UUID vehicleId) {
		ParkingSpotModel parkingSpot = parkingSpotRepository.findById(parkingSpotId)
				.orElseThrow(NotFoundException::new);
		VehicleModel vehicle = vehicleRepository.findById(vehicleId).orElseThrow(NotFoundException::new);

		if (!parkingSpot.getVehicles().contains(vehicle)) {
			throw new BadRequestException("Invalid or inexisting vehicle");
		}

		parkingSpot.removeVehicles(vehicle);
		parkingSpotRepository.save(parkingSpot);

		return parkingSpot;
	}
}
