package com.api.crudapi.models;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "TB_VEHICLE")
public class VehicleModel implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(nullable = false, length = 100)
	private String brand;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "parkingSpot_id", referencedColumnName = "id")
	private ParkingSpotModel parkingSpot;

	
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
	
	public ParkingSpotModel getParkingSpot() {
		return parkingSpot;
	}
	
	public void setParkingSpot(ParkingSpotModel parkingSpot) {
		this.parkingSpot = parkingSpot;
	}
	
}
