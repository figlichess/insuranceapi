package com.example.insuranceapi.converter;

import com.example.insuranceapi.model.RiskParameter;
import com.example.insuranceapi.model.Vehicle;
import dto.RiskParamDTO;
import dto.VehicleDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DTOConverter {
    public Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber(vehicleDTO.getPlateNumber());
        vehicle.setFirstRegistration(vehicleDTO.getFirstRegistration());
        vehicle.setPurchasePrice(vehicleDTO.getPurchasePrice());
        vehicle.setProducer(vehicleDTO.getProducer());
        vehicle.setMileage(vehicleDTO.getMileage());
        vehicle.setPreviousIndemnity(vehicleDTO.getPreviousIndemnity());
        return vehicle;
    }

    public VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setPlateNumber(vehicle.getPlateNumber());
        vehicleDTO.setFirstRegistration(vehicle.getFirstRegistration());
        vehicleDTO.setPurchasePrice(vehicle.getPurchasePrice());
        vehicleDTO.setProducer(vehicle.getProducer());
        vehicleDTO.setMileage(vehicle.getMileage());
        vehicleDTO.setPreviousIndemnity(vehicle.getPreviousIndemnity());
        if (vehicle.getCascoInsurance() != null) {
            vehicleDTO.setCascoAnnualPayment(vehicle.getCascoInsurance().getAnnualPayment());
        }
        if (vehicle.getCascoInsurance() != null) {
            Set<String> riskParams = new HashSet<>();
            for (RiskParameter param : vehicle.getCascoInsurance().getRiskparameters()) {
                riskParams.add(param.getType());
            }
            vehicleDTO.setRiskParams(riskParams);
        }
        return vehicleDTO;

    }

    public RiskParamDTO convertToRiskParamDTO(RiskParameter riskparam) {
        RiskParamDTO riskparamDTO = new RiskParamDTO();
        riskparamDTO.setId(riskparam.getId());
        riskparamDTO.setType(riskparam.getType());
        riskparamDTO.setVehicleParamType(riskparam.getVehicleParamType());
        riskparamDTO.setValue(riskparam.getValue());
        riskparamDTO.setFromFormula(riskparam.isFromFormula());
        riskparamDTO.setHasCasco(riskparam.getCascoinsurances() != null
                && !riskparam.getCascoinsurances().isEmpty());
        return riskparamDTO;

    }

    public RiskParameter convertToRiskParameter(RiskParamDTO riskparamDTO) {
        RiskParameter riskparam = new RiskParameter(riskparamDTO.getType(), riskparamDTO.getVehicleParamType(),
                riskparamDTO.getValue(), riskparamDTO.isFromFormula());
        return riskparam;

    }
}
