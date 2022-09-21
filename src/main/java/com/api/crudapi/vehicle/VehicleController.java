package com.api.crudapi.vehicle;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.crudapi.vehicle.payload.VehicleDto;
import com.api.crudapi.vehicle.payload.VehicleResponse;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

	final VehicleService vehicleService;

	public VehicleController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	@PostMapping
	public ResponseEntity<VehicleModel> saveVehicle(@RequestBody @Valid VehicleDto vehicleDto, Authentication auth) {
		String email = auth.getName();

		var vehicle = vehicleService.save(vehicleDto, email);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(vehicle.getId())
				.toUri();

		return ResponseEntity.created(location).body(vehicle);
	}

	@GetMapping
	public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
		return ResponseEntity.ok().body(vehicleService.getAllVehicles());
	}

	@GetMapping("/{id}")
	public ResponseEntity<VehicleModel> getOneVehicle(@PathVariable(value = "id") UUID id) {
		return ResponseEntity.ok().body(vehicleService.getOneVehicle(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<VehicleModel> updateVehicle(@RequestBody @Valid VehicleDto vehicleDto,
			@PathVariable(value = "id") UUID id) {
		var updatedVehicle = vehicleService.updateVehicle(id, vehicleDto);

		return ResponseEntity.ok().body(updatedVehicle);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<VehicleModel> deleteVehicle(@PathVariable(value = "id") UUID id) {
		vehicleService.deleteVehicle(id);
		return ResponseEntity.noContent().build();
	}
}
