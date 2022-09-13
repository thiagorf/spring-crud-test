package com.api.crudapi.vehicle;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

	final VehicleService vehicleService;
	
	public VehicleController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}
	
	@PostMapping
	public ResponseEntity<VehicleModel> saveVehicle(@RequestBody @Valid VehicleDto vehicleDto) {
		return vehicleService.save(vehicleDto);
	}
	
	@GetMapping
	public ResponseEntity<List<VehicleModel>> getAllVehicles() {
		return vehicleService.getAllVehicles();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<VehicleModel> getOneVehicle(@PathVariable(value = "id") UUID id) {
		return vehicleService.getOneVehicle(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<VehicleModel> updateVehicle(@RequestBody @Valid VehicleDto vehicleDto, @PathVariable(value = "id") UUID id) {
		return vehicleService.updateVehicle(id, vehicleDto);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<VehicleModel> deleteVehicle(@PathVariable(value = "id") UUID id) {
		return vehicleService.deleteVehicle(id);
	}
}
