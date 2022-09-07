package com.api.crudapi.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "TB_PARKING_SPOT")
public class ParkingSpotModel  implements Serializable{
	private static final long  serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@OneToMany(mappedBy = "parkingSpot")
	private Set<VehicleModel> vehicles = new HashSet<>();

	private int spotsQuantity = 10;
	
	@Column(nullable = false, unique = true, length = 10)
	private String parkingSpotNumber;
	
	@Column(nullable = false , length = 30)
	private String apartment;
	
	@Column(nullable = false, length = 30)
	private String block;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public int getSpotsQuantity() {
		return spotsQuantity;
	}

	public void setSpotsQuantity(int spotsQuantity) {
		this.spotsQuantity = spotsQuantity;
	}
	
	public Set<VehicleModel> getVehicles() {
		return vehicles;
	}
	
	
	public void setVehicles(VehicleModel vehicle) {
		this.vehicles.add(vehicle);
		vehicle.setParkingSpot(this);
	}
	
}
