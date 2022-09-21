package com.api.crudapi.vehicle.payload;

import javax.validation.constraints.NotBlank;

public class VehicleDto {
	
	@NotBlank
	private String brand;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
}
