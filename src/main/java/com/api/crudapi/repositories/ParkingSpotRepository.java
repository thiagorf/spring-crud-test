package com.api.crudapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.crudapi.models.ParkingSpotModel;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID>{

	boolean existsByLicensePlateCar(String licensePlate);
	boolean existsByParkingSpotNumber(String parkingSportNumber);
	boolean existsByApartmentAndBlock(String apartment, String block);
}
