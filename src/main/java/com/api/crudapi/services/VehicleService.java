package com.api.crudapi.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;

import com.api.crudapi.dtos.VehicleDto;
import com.api.crudapi.models.VehicleModel;
import com.api.crudapi.repositories.VehicleRepository;

public class VehicleService {

	final VehicleRepository vehicleRepository;
	
	public VehicleService(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}
	
	public void save(VehicleDto vehicleDto) {
		var vehicleModel = new VehicleModel();
		
		BeanUtils.copyProperties(vehicleDto, vehicleModel);
		this.vehicleRepository.save(vehicleModel);
	}
	
	public ResponseEntity<List<VehicleModel>>  getAllVehicles() {
		var vehicles = this.vehicleRepository.findAll();
		
		return ResponseEntity.ok().body(vehicles);
	}
}
