package com.example.insuranceapi.casco;

import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.AdditionalRiskParameter;
import com.example.insuranceapi.model.RiskParameter;
import com.example.insuranceapi.model.Vehicle;
import com.example.insuranceapi.service.AdditionalRiskParameterService;
import com.example.insuranceapi.service.CascoInsuranceService;
import com.example.insuranceapi.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CascoCalculatorTest {

    @Mock
    private AdditionalRiskParameterService addRiskParameterService;

    @Mock
    private VehicleService vehicleService;
    @Mock
    private CascoInsuranceService cascoInsuranceService;

    @InjectMocks
    private CascoCalculator cascoCalculator;


    private Vehicle vehicle;
    private Vehicle vehicle2;
    private Vehicle vehicle3;
    private List<Vehicle> vehicles;
    private Set<RiskParameter> riskParameters;
    private AdditionalRiskParameter makeCoefficient;
    private AdditionalRiskParameter avgPurchasePrice;
    private Set<AdditionalRiskParameter> addRiskParams;
    private RiskParameter vehicleAge;
    private RiskParameter previousIndemnity;
    private RiskParameter currentValue;

    @BeforeEach
    public void init() {
        vehicle = new Vehicle("789abc", 2013, 64533, "VOLVO", 192719, 0.0);
        vehicle2 = new Vehicle("789abc", 2013, 64533, "FORD", 192719, 0.0);
        vehicle3 = new Vehicle("789abc", 2013, 64533, "FORD", 192719, 0.0);
        vehicles = new ArrayList<>();
        vehicles.add(vehicle);
        vehicles.add(vehicle2);
        vehicles.add(vehicle3);
        vehicleAge = new RiskParameter("vehicle_age", "vehicle_age", 1.1, false);
        previousIndemnity = new RiskParameter("previous_indemnity", "previous_indemnity", 0.05, false);
        currentValue = new RiskParameter("current_value", "purchase_price", 1, true);
        riskParameters = new HashSet<RiskParameter>();
        riskParameters.add(vehicleAge);
        riskParameters.add(previousIndemnity);
        riskParameters.add(currentValue);
        makeCoefficient = new AdditionalRiskParameter("volvo", "make_coefficients", 0.95);
        avgPurchasePrice = new AdditionalRiskParameter("VOLVO", "avg_purchase_price", 44225);
        addRiskParams = new HashSet<AdditionalRiskParameter>();
        addRiskParams.add(makeCoefficient);
        addRiskParams.add(avgPurchasePrice);

    }

    @Test
    void calculateAnnualCasco() {
        try {
            when(addRiskParameterService.findAdditionalRiskParameterByTypeProducer(eq("make_coefficients"), eq(vehicle.getProducer()))).thenReturn(makeCoefficient);
            when(addRiskParameterService.findAdditionalRiskParameterByTypeProducer(eq("avg_purchase_price"), eq(vehicle.getProducer()))).thenReturn(avgPurchasePrice);

            cascoCalculator.calculateAnnualCasco(vehicle, riskParameters, null);
        } catch (ResourceNotFoundException exception) {
            fail("Caught an ResourceNotFoundException");
        }
        assertEquals(19201.098099158513, vehicle.getCascoInsurance().getAnnualPayment());
    }

    @Test
    void calculateAnnualCascoWithExistingAddParams() {
        try {
            when(addRiskParameterService.findAddRiskByTypeProducerFromSet(eq(addRiskParams), eq("make_coefficients"), eq(vehicle.getProducer()))).thenReturn(makeCoefficient);
            when(addRiskParameterService.findAddRiskByTypeProducerFromSet(eq(addRiskParams), eq("avg_purchase_price"), eq(vehicle.getProducer()))).thenReturn(avgPurchasePrice);
            cascoCalculator.calculateAnnualCasco(vehicle, riskParameters, addRiskParams);
        } catch (ResourceNotFoundException exception) {
            fail("Caught an ResourceNotFoundException");
        }
        assertEquals(19201.098099158513, vehicle.getCascoInsurance().getAnnualPayment());
        assertEquals(3, vehicle.getCascoInsurance().getRiskparameters().size());
    }

    @Test
    void sumRiskParameterValue() {
        double cascoPayment = cascoCalculator.sumRiskParameterValue(0, vehicle, vehicleAge);
        assertEquals(8.8, cascoPayment);
    }

    @Test
    void calculateVehicleCurrentValue() {
        double currentValue = cascoCalculator.calculateVehicleCurrentValue(Calendar.getInstance().get(Calendar.YEAR)
                - vehicle.getFirstRegistration(), vehicle.getMileage());
        assertEquals(0.3130628083250514, currentValue);
    }
}
