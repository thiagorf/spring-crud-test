package com.api.crudapi.controllers;


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

import com.api.crudapi.dtos.ParkingSpotDto;
import com.api.crudapi.models.ParkingSpotModel;
import com.api.crudapi.services.ParkingSpotService;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

	final ParkingSpotService parkingSpotService;
	
	public ParkingSpotController(ParkingSpotService parkingSpotService) {
		this.parkingSpotService = parkingSpotService;
	}
	
	@PostMapping
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
		return this.parkingSpotService.addParkingSpot(parkingSpotDto);
	}
	
	@GetMapping
	public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return this.parkingSpotService.findAllParkingSpots(pageable);	
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ParkingSpotModel> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
		return this.parkingSpotService.findOneParkingSpot(id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ParkingSpotModel> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
		return this.parkingSpotService.deleteParkingSpot(id);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ParkingSpotModel> updateParkingSpot(@PathVariable(value = "id") UUID id, @RequestBody @Valid ParkingSpotDto parkingSpotDto) {
		return this.parkingSpotService.updateParkingSpot(id, parkingSpotDto);
	}
	
	// attach parkingspot id to vehicle and show the relantionship created?
	@PutMapping("/{parkingSpot_id}/vehicles/{vehicle_id}")
	public ResponseEntity<ParkingSpotModel> parkVehicle(@PathVariable("parkingSpot_id") UUID parkingSpotId, @PathVariable("vehicle_id") UUID vehicleId) {
		
		return parkingSpotService.parkVehicle(parkingSpotId, vehicleId);
	}
	
	
}
