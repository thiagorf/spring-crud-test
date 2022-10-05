package com.api.crudapi.parkingspot.payload;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.api.crudapi.vehicle.VehicleModel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ParkingSpotCarsResponse {

	private UUID id;
	private int spotsQuantity;
	private String parkingSpotNumber;
	private String apartment;
	private String block;
	@Builder.Default
	private Set<VehicleModel> vehicles = new HashSet<>();

}
