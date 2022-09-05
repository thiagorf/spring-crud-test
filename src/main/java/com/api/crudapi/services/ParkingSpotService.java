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
import com.api.crudapi.repositories.ParkingSpotRepository;

@Service
public class ParkingSpotService {
	
	final ParkingSpotRepository parkingSpotRepository;
	
	public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
		this.parkingSpotRepository = parkingSpotRepository;
	}
	
	public ResponseEntity<Object> addParkingSpot(ParkingSpotDto parkingSpotDto) {
		var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(parkingSpotModel.getId())
				.toUri();		
		
		return ResponseEntity.created(location).body(parkingSpotModel);
	}
	
	@Transactional
	public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
		return parkingSpotRepository.save(parkingSpotModel);
	}

	public ResponseEntity<Page<ParkingSpotModel>> findAllParkingSpots(Pageable pageable) {
		return ResponseEntity.ok().body(parkingSpotRepository.findAll(pageable));
	}
	
	public ResponseEntity<ParkingSpotModel> findOneParkingSpot(UUID id) {
		Optional<ParkingSpotModel> parkingSpot = parkingSpotRepository.findById(id);
		
		if(!parkingSpot.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(parkingSpot.get());
	}
	
	@Transactional
	public ResponseEntity<ParkingSpotModel> deleteParkingSpot(UUID id) {
		Optional<ParkingSpotModel> parkingSpot = parkingSpotRepository.findById(id);
		
		if(!parkingSpot.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
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
	
}
