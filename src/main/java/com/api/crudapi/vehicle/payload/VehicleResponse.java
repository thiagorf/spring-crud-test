package com.api.crudapi.vehicle.payload;

import java.util.UUID;

import com.api.crudapi.vehicle.VehicleModel;

public class VehicleResponse {

	private UUID id;
	private String brand;
	private UUID user_id;

	public VehicleResponse(VehicleModel vehicle, UUID userId) {
		this.id = vehicle.getId();
		this.brand = vehicle.getBrand();
		this.user_id = userId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public UUID getUser_id() {
		return user_id;
	}

	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}

}
