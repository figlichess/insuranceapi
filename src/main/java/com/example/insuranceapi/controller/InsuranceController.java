package com.example.insuranceapi.controller;

import com.example.insuranceapi.casco.CascoCalculator;
import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.RiskParameter;
import com.example.insuranceapi.model.Vehicle;
import com.example.insuranceapi.service.RiskParameterService;
import com.example.insuranceapi.service.VehicleService;
import com.example.insuranceapi.dto.RiskParamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/v1/insurance")
public class InsuranceController {
    @Autowired
    private CascoCalculator cascoCalculator;

    @Autowired
    private RiskParameterService riskParameterService;

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/calculatecasco")
    public Map<String, Boolean> calculateCascoInsurance(@RequestBody RiskParamDTO[] riskDTOs) throws ResourceNotFoundException {
        Set<RiskParameter> riskParameters = new HashSet<>();
        for (RiskParamDTO riskDto : riskDTOs) {
            riskParameters.add(riskParameterService.getRiskParameter(riskDto.getId()));
        }
        List<Vehicle> vehicles = vehicleService.getVehicles();
        Set<Vehicle> vehicleSet = new HashSet<>(vehicles);
        cascoCalculator.calculateAnnualCascoForVehiclesSet(vehicleSet, riskParameters, null);
        Map<String, Boolean> response = new HashMap<>();
        response.put("calculated casco", Boolean.TRUE);
        return response;
    }
}
