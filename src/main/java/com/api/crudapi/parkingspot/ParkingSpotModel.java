package com.api.crudapi.parkingspot;

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

import com.api.crudapi.vehicle.VehicleModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_PARKING_SPOT")
public class ParkingSpotModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@JsonManagedReference
	@OneToMany(mappedBy = "parkingSpot")
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@Builder.Default
	private Set<VehicleModel> vehicles = new HashSet<>();

	@Column
	@Builder.Default
	private int spotsQuantity = 10;

	@Column(nullable = false, unique = true, length = 10)
	private String parkingSpotNumber;

	@Column(nullable = false, length = 30)
	private String apartment;

	@Column(nullable = false, length = 30)
	private String block;

	public Set<VehicleModel> getVehicles() {
		return vehicles;
	}

	public void setVehicles(VehicleModel vehicle) {
		this.vehicles.add(vehicle);
		vehicle.setParkingSpot(this);
	}

	public void removeVehicles(VehicleModel vehicle) {
		vehicles.remove(vehicle);
		vehicle.setParkingSpot(null);
	}

}
