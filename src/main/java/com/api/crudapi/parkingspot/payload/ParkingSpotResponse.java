package com.api.crudapi.parkingspot.payload;

import java.util.UUID;

import com.api.crudapi.parkingspot.ParkingSpotModel;

import lombok.Getter;
import lombok.Setter;

// Maybe getters and setters are useless in tthis case?
// Serialization is only possible with getters and setters
@Getter
@Setter
public class ParkingSpotResponse {

	UUID id;
	int spotsQuantity;
	String parkingSpotNumber;
	String apartment;
	String block;
	int parkedCars;

	public ParkingSpotResponse(ParkingSpotModel parkingSpot) {
		this.id = parkingSpot.getId();
		this.spotsQuantity = parkingSpot.getSpotsQuantity();
		this.parkingSpotNumber = parkingSpot.getParkingSpotNumber();
		this.apartment = parkingSpot.getApartment();
		this.block = parkingSpot.getBlock();
		this.parkedCars = parkingSpot.getVehicles().size();
	}
}
