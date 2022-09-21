package com.api.crudapi.parkingspot.payload;

import java.util.UUID;

import com.api.crudapi.parkingspot.ParkingSpotModel;

// Maybe getters and setters are useless in tthis case?
// Serialization is only possible with getters and setters
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

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getSpotsQuantity() {
		return spotsQuantity;
	}

	public void setSpotsQuantity(int spotsQuantity) {
		this.spotsQuantity = spotsQuantity;
	}

	public String getParkingSpotNumber() {
		return parkingSpotNumber;
	}

	public void setParkingSpotNumber(String parkingSpotNumber) {
		this.parkingSpotNumber = parkingSpotNumber;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public int getParkedCars() {
		return parkedCars;
	}

	public void setParkedCars(int parkedCars) {
		this.parkedCars = parkedCars;
	}

}
