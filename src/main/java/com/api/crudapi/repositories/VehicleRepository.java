package com.api.crudapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.crudapi.models.VehicleModel;

public interface VehicleRepository extends JpaRepository<VehicleModel, UUID> {

}
