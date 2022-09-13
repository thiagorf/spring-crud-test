package com.api.crudapi.parkingspot;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID>{

}
