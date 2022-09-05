
package com.api.crudapi.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.crudapi.dtos.VehicleDto;
import com.api.crudapi.models.VehicleModel;
import com.api.crudapi.services.VehicleService;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

	final VehicleService vehicleService;
	
	public VehicleController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}
	
	@PostMapping
	public ResponseEntity<VehicleModel> saveVehicle(@RequestBody @Valid VehicleDto vehicleDto) {
		return this.vehicleService.save(vehicleDto);
	}
	
	@GetMapping
	public ResponseEntity<List<VehicleModel>> getAllVehicles() {
		return this.vehicleService.getAllVehicles();
	}
}
