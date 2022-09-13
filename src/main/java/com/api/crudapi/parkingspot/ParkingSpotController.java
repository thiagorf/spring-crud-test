package com.api.crudapi.parkingspot;


import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;



@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

	final ParkingSpotService parkingSpotService;
	
	public ParkingSpotController(ParkingSpotService parkingSpotService) {
		this.parkingSpotService = parkingSpotService;
	}
	
	@PostMapping
	public ResponseEntity<ParkingSpotModel> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
		var parkingSpot = parkingSpotService.addParkingSpot(parkingSpotDto);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(parkingSpot.getId())
				.toUri();	
		
		return ResponseEntity.created(location).body(parkingSpot);
	}
	
	@GetMapping
	public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		var parkingSpot = parkingSpotService.findAllParkingSpots(pageable);
		
		return ResponseEntity.ok().body(parkingSpot);	
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<ParkingSpotCarsResponse> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
		var parkingSpot = parkingSpotService.findOneParkingSpot(id);
		return ResponseEntity.ok().body(parkingSpot);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ParkingSpotModel> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
		parkingSpotService.deleteParkingSpot(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ParkingSpotModel> updateParkingSpot(@PathVariable(value = "id") UUID id, @RequestBody @Valid ParkingSpotDto parkingSpotDto) {
		var updatedParkingSpot = parkingSpotService.updateParkingSpot(id, parkingSpotDto);
		
		return ResponseEntity.ok().body(updatedParkingSpot);
	}
	
	// attach parkingspot id to vehicle and show the relantionship created?
	@PutMapping("/{parkingSpot_id}/vehicles/{vehicle_id}")
	public ResponseEntity<ParkingSpotModel> parkVehicle(@PathVariable("parkingSpot_id") UUID parkingSpotId, @PathVariable("vehicle_id") UUID vehicleId) {
		var parkingSpotVehicles = parkingSpotService.parkVehicle(parkingSpotId, vehicleId);
		
		return ResponseEntity.ok().body(parkingSpotVehicles);
	}
	
	@PutMapping("/{parkingSpot_id}/vehicles/{vehicle_id}/leave")
	public ResponseEntity<Object> leaveParkingSpot(@PathVariable("parkingSpot_id") UUID parkingSpotId, @PathVariable("vehicle_id") UUID vehicleId) {
		var parkingSpotVehicles = parkingSpotService.leaveParkingSpot(parkingSpotId, vehicleId);
		
		return ResponseEntity.ok().body(parkingSpotVehicles);
	}
	
}
