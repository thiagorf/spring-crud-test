package com.api.crudapi.parkingspot;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.api.crudapi.vehicle.VehicleModel;



public class ParkingSpotCarsResponse {
	
	private UUID id;
	private int spotsQuantity;
	private String parkingSpotNumber;
	private String apartment;
	private String block;
	private Set<VehicleModel> vehicles = new HashSet<>();

	

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

	public Set<VehicleModel> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Set<VehicleModel> vehicles) {
		this.vehicles = vehicles;
	}

	public String getParkingSpotNumber() {
		return parkingSpotNumber;
	}

	public void setParkingSpotNumber(String parkingSpotNumber) {
		this.parkingSpotNumber = parkingSpotNumber;
	}
	

}
