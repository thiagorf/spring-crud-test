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

@Entity
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
	private Set<VehicleModel> vehicles = new HashSet<>();

	public Set<VehicleModel> getVehicles() {
		return vehicles;
	}

	public void addVehicles(VehicleModel vehicles) {
		this.vehicles.add(vehicles);
		vehicles.setUser(this);
	}

	// TODO remove vehicles?

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	// TODO oneToMany relationship

}
