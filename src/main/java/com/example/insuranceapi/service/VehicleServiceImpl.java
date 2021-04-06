package com.example.insuranceapi.service;

import com.example.insuranceapi.casco.CascoCalculator;
import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.AdditionalRiskParameter;
import com.example.insuranceapi.model.Vehicle;
import com.example.insuranceapi.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CascoInsuranceService cascoInsuranceService;

    @Autowired
    private CascoCalculator cascoCalculator;

    @Autowired
    private AdditionalRiskParameterService addRiskParameterService;

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public void deleteVehicle(Integer id) throws ResourceNotFoundException {
        Vehicle vehicle = getVehicle(id);
        if (vehicle.getCascoInsurance() != null) {
            cascoInsuranceService.removeRisks(vehicle.getCascoInsurance());
        }
        vehicleRepository.delete(vehicle);
    }

    @Override
    public Vehicle updateVehicle(Integer id, Vehicle vehicleData) throws ResourceNotFoundException {
        Vehicle vehicle = getVehicle(id);
        vehicle.setPlateNumber(vehicleData.getPlateNumber());
        vehicle.setFirstRegistration(vehicleData.getFirstRegistration());
        vehicle.setPurchasePrice(vehicleData.getPurchasePrice());
        vehicle.setProducer(vehicleData.getProducer());
        vehicle.setMileage(vehicleData.getMileage());
        vehicle.setPreviousIndemnity(vehicleData.getPreviousIndemnity());
        //recalculating casco insurance
        if (vehicle.getCascoInsurance() != null) {
            List<AdditionalRiskParameter> addRisks = addRiskParameterService.getAdditionalRiskParameters();
            Set<AdditionalRiskParameter> addrisksSet = new HashSet<>(addRisks);
            cascoCalculator.calculateAnnualCasco(vehicleData, vehicle.getCascoInsurance().getRiskparameters(), addrisksSet);
            vehicle.getCascoInsurance().setAnnualPayment(vehicleData.getCascoInsurance().getAnnualPayment());
        }
        final Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return updatedVehicle;
    }

    @Override
    public Vehicle getVehicle(Integer id) throws ResourceNotFoundException {
        return vehicleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found for this id :: " + id));
    }

    @Override
    public void batchVehicles(Set<Vehicle> vehicles) {
        Set<Vehicle> vehiclesToBatch = new HashSet<>();

        for (Vehicle vehicle : vehicles) {
            vehiclesToBatch.add(vehicle);

            if (vehiclesToBatch.size() % batchSize == 0 && vehiclesToBatch.size() > 0) {
                vehicleRepository.saveAll(vehiclesToBatch);
                vehiclesToBatch.clear();
            }
        }

        if (vehiclesToBatch.size() > 0) {
            vehicleRepository.saveAll(vehiclesToBatch);
            vehiclesToBatch.clear();
        }
    }

    @Override
    public Page<Vehicle> getVehiclePages(PageRequest pageRequest) {
        return vehicleRepository.findAll(pageRequest);
    }
}
