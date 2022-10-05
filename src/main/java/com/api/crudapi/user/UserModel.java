package com.api.crudapi.user;

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

import com.api.crudapi.vehicle.VehicleModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column
	private String name;

	@Column(unique = true)
	private String email;

	@Column
	private String password;

	@JsonManagedReference
	@OneToMany(mappedBy = "user")
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<VehicleModel> vehicles = new HashSet<>();

	public Set<VehicleModel> getVehicles() {
		return vehicles;
	}

	public void addVehicles(VehicleModel vehicles) {
		this.vehicles.add(vehicles);
		vehicles.setUser(this);
	}

	// TODO remove vehicles?

}
