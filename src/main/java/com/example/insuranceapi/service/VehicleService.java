package com.example.insuranceapi.service;

import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

public interface VehicleService {
    public Vehicle saveVehicle(Vehicle vehicle);

    public List<Vehicle> getVehicles();

    public Page<Vehicle> getVehiclePages(PageRequest pageRequest);

    public void deleteVehicle(Integer id) throws ResourceNotFoundException;

    public Vehicle getVehicle(Integer id) throws ResourceNotFoundException;

    public Vehicle updateVehicle(Integer id, Vehicle vehicle) throws ResourceNotFoundException;

    public void batchVehicles(Set<Vehicle> vehicles);

}
