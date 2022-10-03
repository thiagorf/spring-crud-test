package com.api.crudapi.vehicle;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.crudapi.exceptions.NotFoundException;
import com.api.crudapi.user.UserModel;
import com.api.crudapi.user.UserRepository;
import com.api.crudapi.vehicle.payload.VehicleDto;
import com.api.crudapi.vehicle.payload.VehicleResponse;

@Service
public class VehicleService {

	final private VehicleRepository vehicleRepository;
	final private UserRepository userRepository;
	final private ModelMapper modelMapper;

	public VehicleService(VehicleRepository vehicleRepository, ModelMapper modelMapper, UserRepository userRepository) {
		this.vehicleRepository = vehicleRepository;
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
	}

	@Transactional
	public VehicleModel save(VehicleDto vehicleDto, String email) {
		UserModel user = userRepository.findByEmail(email).orElseThrow(NotFoundException::new);

		var vehicleModel = new VehicleModel();

		BeanUtils.copyProperties(vehicleDto, vehicleModel);
		vehicleModel.setUser(user);

		return vehicleRepository.save(vehicleModel);

	}

	public List<VehicleResponse> getAllVehicles() {
		List<VehicleModel> vehicleModel = vehicleRepository.findAll();

		return vehicleModel.stream().map(vehicle -> new VehicleResponse(vehicle, vehicle.getUser().getId()))
				.collect(Collectors.toList());
	}

	public VehicleModel getOneVehicle(UUID id) {
		VehicleModel vehicleModel = vehicleRepository.findById(id).orElseThrow(NotFoundException::new);

		return vehicleModel;
	}

	public VehicleModel updateVehicle(UUID id, VehicleDto vehicleDto) {
		VehicleModel vehicleModel = vehicleRepository.findById(id).orElseThrow(NotFoundException::new);

		modelMapper.map(vehicleDto, vehicleModel);

		return vehicleRepository.save(vehicleModel);

	}

	@Transactional
	public void deleteVehicle(UUID id) {
		VehicleModel vehicleModel = vehicleRepository.findById(id).orElseThrow(NotFoundException::new);

		vehicleRepository.delete(vehicleModel);
	}

}
