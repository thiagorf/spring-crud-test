package com.api.crudapi.services;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
		this.vehicleRepository.save(vehicleModel);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(vehicleModel.getId())
				.toUri();
		
		
		return ResponseEntity.created(location).body(vehicleModel);
	}
	
	public ResponseEntity<List<VehicleModel>>  getAllVehicles() {
		var vehicles = this.vehicleRepository.findAll();
		
		return ResponseEntity.ok().body(vehicles);
	}
	
}
