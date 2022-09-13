package com.api.crudapi.parkingspot;

import javax.validation.constraints.NotBlank;

public class ParkingSpotDto {

	@NotBlank
	private String parkingSpotNumber;
	
	@NotBlank
	private String apartment;
	
	@NotBlank
	private String block;

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
	
}
