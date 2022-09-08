package com.api.crudapi.services;


import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.crudapi.dtos.ParkingSpotDto;
import com.api.crudapi.models.ParkingSpotModel;
import com.api.crudapi.models.VehicleModel;
import com.api.crudapi.repositories.ParkingSpotRepository;
import com.api.crudapi.repositories.VehicleRepository;
import com.api.crudapi.responses.ParkingSpotCarsResponse;

@Service
public class ParkingSpotService {
	
	final ParkingSpotRepository parkingSpotRepository;
	final VehicleRepository vehicleRepository;
	
	public ParkingSpotService(ParkingSpotRepository parkingSpotRepository, VehicleRepository vehicleRepository) {
		this.parkingSpotRepository = parkingSpotRepository;
		this.vehicleRepository = vehicleRepository;
	}
	
	public ResponseEntity<Object> addParkingSpot(ParkingSpotDto parkingSpotDto) {
		var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(parkingSpotModel.getId())
				.toUri();		
		
		return ResponseEntity.created(location).body(parkingSpotRepository.save(parkingSpotModel));
	}
	
	@Transactional
	public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
		return parkingSpotRepository.save(parkingSpotModel);
	}

	public ResponseEntity<Page<ParkingSpotModel>> findAllParkingSpots(Pageable pageable) {
		return ResponseEntity.ok().body(parkingSpotRepository.findAll(pageable));
	}
	
	public ResponseEntity<ParkingSpotCarsResponse> findOneParkingSpot(UUID id) {
		Optional<ParkingSpotModel> parkingSpot = parkingSpotRepository.findById(id);
		
		if(!parkingSpot.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		var parkingSpotCars = new ParkingSpotCarsResponse();
		BeanUtils.copyProperties(parkingSpot, parkingSpotCars);
		parkingSpotCars.setVehicles(parkingSpot.get().getVehicles());
		
		return ResponseEntity.ok().body(parkingSpotCars);
	}
	
	@Transactional
	public ResponseEntity<ParkingSpotModel> deleteParkingSpot(UUID id) {
		Optional<ParkingSpotModel> parkingSpot = parkingSpotRepository.findById(id);
		
		if(!parkingSpot.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		parkingSpotRepository.delete(parkingSpot.get());
		
		return ResponseEntity.noContent().build();
	}
	
	public ResponseEntity<ParkingSpotModel> updateParkingSpot(UUID id, ParkingSpotDto parkingSpotDto) {
		Optional<ParkingSpotModel> parkingSpot = parkingSpotRepository.findById(id);
		
		if(!parkingSpot.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		parkingSpotModel.setId(parkingSpot.get().getId());
		
		return ResponseEntity.ok().body(parkingSpotRepository.save(parkingSpotModel));
	}
	
	public ResponseEntity<ParkingSpotModel> parkVehicle(UUID parkingSpotId, UUID vehicleId) {
		Optional<ParkingSpotModel> parkingSpot = parkingSpotRepository.findById(parkingSpotId);
		
		if(!parkingSpot.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Optional<VehicleModel> vehicle = vehicleRepository.findById(vehicleId);
		
		if(!vehicle.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		
		vehicle.get().setParkingSpot(parkingSpot.get());
		vehicleRepository.save(vehicle.get());
		
		return ResponseEntity.ok().body(parkingSpot.get());
		
	}
	
	public ResponseEntity<Object> leaveParkingSpot(UUID parkingSpotId, UUID vehicleId ) {
		Optional<ParkingSpotModel> parkingSpot = parkingSpotRepository.findById(parkingSpotId);
		
		if(!parkingSpot.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Optional<VehicleModel> vehicle = vehicleRepository.findById(vehicleId);
		
		if(!vehicle.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		var vehicleModel = vehicle.get();
		var parkingSpotModel = parkingSpot.get();
		
		if(!parkingSpotModel.getVehicles().contains(vehicleModel)) {
			return ResponseEntity.badRequest().body("Invalid or iniexisting vehicle!");
		}
		
		parkingSpotModel.removeVehicles(vehicleModel);
		parkingSpotRepository.save(parkingSpotModel);
		
		return ResponseEntity.ok().body(parkingSpotModel);
	}
}
