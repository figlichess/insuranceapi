package com.example.insuranceapi.casco;

import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.AdditionalRiskParameter;
import com.example.insuranceapi.model.CascoInsurance;
import com.example.insuranceapi.model.RiskParameter;
import com.example.insuranceapi.model.Vehicle;
import com.example.insuranceapi.service.AdditionalRiskParameterService;
import com.example.insuranceapi.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Component
@Transactional
public class CascoCalculator {

    Logger logger = LoggerFactory.getLogger(CascoCalculator.class);
    @Autowired
    private AdditionalRiskParameterService addRiskParameterService;
    @Autowired
    private VehicleService vehicleService;

    @Transactional
    public void calculateAnnualCascoForVehiclesSet(Set<Vehicle> vehicles, Set<RiskParameter> riskparameters, Set<AdditionalRiskParameter> additionalRiskparameters) throws ResourceNotFoundException {
        for (Vehicle vehicle : vehicles) {
            calculateAnnualCasco(vehicle, riskparameters, additionalRiskparameters);
        }
        vehicleService.batchVehicles(vehicles);
    }

    /**
     * Calculates vehicles insurance based on riskparameters and
     * additionalriskparameters data.
     *
     * @param vehicle
     * @param riskparameters
     * @param additionalRiskparameters
     * @throws ResourceNotFoundException
     */
    @Transactional
    public void calculateAnnualCasco(Vehicle vehicle, Set<RiskParameter> riskparameters, Set<AdditionalRiskParameter> additionalRiskparameters) throws ResourceNotFoundException {
        double cascoPayment = 0;
        CascoInsurance cascoInsurance = vehicle.getCascoInsurance() != null ? vehicle.getCascoInsurance() : new CascoInsurance();
        AdditionalRiskParameter avgPurchasePrice =
                additionalRiskparameters != null ? addRiskParameterService.findAddRiskByTypeProducerFromSet(additionalRiskparameters, "avg_purchase_price", vehicle.getProducer()) :
                        addRiskParameterService.findAdditionalRiskParameterByTypeProducer("avg_purchase_price", vehicle.getProducer());
        if (avgPurchasePrice != null) {
            AdditionalRiskParameter makeCoef =
                    additionalRiskparameters != null ? addRiskParameterService.findAddRiskByTypeProducerFromSet(additionalRiskparameters, "make_coefficients", vehicle.getProducer()) :
                            addRiskParameterService.findAdditionalRiskParameterByTypeProducer("make_coefficients", vehicle.getProducer());
            cascoInsurance.setRiskparameters(new HashSet<>());
            for (RiskParameter riskParam : riskparameters) {
                cascoPayment = sumRiskParameterValue(cascoPayment, vehicle, riskParam);
                cascoInsurance.getRiskparameters().add(riskParam);
            }
            cascoPayment *= makeCoef != null ? makeCoef.getValue() : 1;
            cascoInsurance.setAnnualPayment(cascoPayment);
            vehicle.setCascoInsurance(cascoInsurance);
        } else {
            logger.warn("No average prices found for vehicle producer " + vehicle.getProducer());
        }
    }

    /**
     * Riskparameter adding calculation
     *
     * @param cascoPayment
     * @param vehicle
     * @param riskParameter
     * @return
     */
    public double sumRiskParameterValue(double cascoPayment, Vehicle vehicle, RiskParameter riskParameter) {
        double riskValue = (riskParameter.isFromFormula() ? getRiskParamValueFromFormula(riskParameter, vehicle) : riskParameter.getValue());
        double vehicleParamValue = getVehicleParamForCasco(vehicle, riskParameter.getVehicleParamType());
        return cascoPayment + (riskValue * vehicleParamValue);
    }

    /**
     * Getting vehicle parameter value to multiply with riskparameter
     *
     * @param vehicle
     * @param vehicleParamType
     * @return
     */
    public double getVehicleParamForCasco(Vehicle vehicle, String vehicleParamType) {
        return switch (vehicleParamType) {
            case "vehicle_age" -> Calendar.getInstance().get(Calendar.YEAR) - vehicle.getFirstRegistration();
            case "previous_indemnity" -> vehicle.getPreviousIndemnity();
            case "vehicle_value" -> vehicle.getPurchasePrice();
            case "purchase_price" -> vehicle.getPurchasePrice();
            case "mileage" -> vehicle.getMileage();
            default -> 1;
        };
    }

    /**
     * Getting riskparameter formula by riskparameter name
     *
     * @param riskParameter
     * @param vehicle
     * @return
     */
    private double getRiskParamValueFromFormula(RiskParameter riskParameter, Vehicle vehicle) {
        return switch (riskParameter.getType()) {
            case "current_value" -> calculateVehicleCurrentValue(Calendar.getInstance().get(Calendar.YEAR)
                    - vehicle.getFirstRegistration(), vehicle.getMileage());
            default -> 1;
        };
    }

    /**
     * Current value parameter calculating formula
     *
     * @param age
     * @param mileage
     * @return
     */
    @Cacheable("currentValue")
    public double calculateVehicleCurrentValue(int age, int mileage) {
        return (102 + (-7.967) * age + (0.8337334) * (Math.pow(age, 2))
                + (-0.07785488) * (Math.pow(age, 3)) + (0.002518395) * (Math.pow(age, 4))
                + (-0.0002236396) * mileage + (3.669157E-10) * (Math.pow(mileage, 2))
                + (-1.81368E-16) * (Math.pow(mileage, 3))) / 100;
    }
}
