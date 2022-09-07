package com.api.crudapi.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.crudapi.dtos.VehicleDto;
import com.api.crudapi.models.VehicleModel;
import com.api.crudapi.repositories.VehicleRepository;

@Service
public class VehicleService {

	final VehicleRepository vehicleRepository;
	
	public VehicleService(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}
	
	@Transactional
	public ResponseEntity<VehicleModel> save(VehicleDto vehicleDto) {
		var vehicleModel = new VehicleModel();
		
		BeanUtils.copyProperties(vehicleDto, vehicleModel);
		vehicleRepository.saveAndFlush(vehicleModel);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(vehicleModel.getId())
				.toUri();
		
		
		return ResponseEntity.created(location).body(vehicleModel);
	}
	
	public ResponseEntity<List<VehicleModel>>  getAllVehicles() {
		var vehicles = vehicleRepository.findAll();
		
		return ResponseEntity.ok().body(vehicles);
	}
	
	public ResponseEntity<VehicleModel> getOneVehicle(UUID id) {
		Optional<VehicleModel> vehicleModel = vehicleRepository.findById(id);
		
		if(!vehicleModel.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(vehicleModel.get());
	}
	
	public ResponseEntity<VehicleModel> updateVehicle(UUID id, VehicleDto vehicleDto) {
		Optional<VehicleModel> vehicle = vehicleRepository.findById(id);
		
		if(!vehicle.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		var vehicleModel= new VehicleModel();
		BeanUtils.copyProperties(vehicleDto, vehicleModel);
		vehicleModel.setId(vehicle.get().getId());
		
		return ResponseEntity.ok().body(vehicleRepository.save(vehicleModel));
		
	}
	
	@Transactional
	public ResponseEntity<VehicleModel> deleteVehicle(UUID id) {
		Optional<VehicleModel> vehicleModel = vehicleRepository.findById(id);
		
		if(!vehicleModel.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		vehicleRepository.delete(vehicleModel.get());
		
		return ResponseEntity.noContent().build();
	}
	
}
