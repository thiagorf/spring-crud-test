package com.api.crudapi.parkingspot;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ParkingSpotDto {

	@NotBlank
	private String parkingSpotNumber;

	@NotBlank
	private String apartment;

	@NotBlank
	private String block;

}
