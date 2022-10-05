package com.api.crudapi.vehicle;

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

import com.api.crudapi.parkingspot.ParkingSpotModel;
import com.api.crudapi.user.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_VEHICLE")
public class VehicleModel implements Serializable {

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

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UserModel user;

}
