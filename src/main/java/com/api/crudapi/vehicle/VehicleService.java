package com.api.crudapi.vehicle;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.crudapi.exceptions.NotFoundException;

@Service
public class VehicleService {

	final VehicleRepository vehicleRepository;
	final ModelMapper modelMapper;
	
	public VehicleService(VehicleRepository vehicleRepository, ModelMapper modelMapper) {
		this.vehicleRepository = vehicleRepository;
		this.modelMapper = modelMapper;
	}
	
	@Transactional
	public VehicleModel save(VehicleDto vehicleDto) {
		var vehicleModel = new VehicleModel();
		
		BeanUtils.copyProperties(vehicleDto, vehicleModel);
		vehicleRepository.saveAndFlush(vehicleModel);
		
		return vehicleModel;
	}
	
	public List<VehicleModel>  getAllVehicles() {
		var vehicleModel = vehicleRepository.findAll();
		
		return vehicleModel;
	}
	
	public VehicleModel getOneVehicle(UUID id) {
		VehicleModel vehicleModel = vehicleRepository.findById(id).orElseThrow(() -> new NotFoundException());
		
		return vehicleModel;
	}
	
	public VehicleModel updateVehicle(UUID id, VehicleDto vehicleDto) {
		VehicleModel vehicleModel = vehicleRepository.findById(id).orElseThrow(() -> new NotFoundException());
		
		modelMapper.map(vehicleDto, vehicleModel);
		
		return vehicleRepository.save(vehicleModel);
		
	}
	
	@Transactional
	public void deleteVehicle(UUID id) {
		VehicleModel vehicleModel = vehicleRepository.findById(id).orElseThrow(() -> new NotFoundException());

		vehicleRepository.delete(vehicleModel);
	}
	
}
