package com.example.insuranceapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class InsuranceDTO {
    private Set<RiskParamDTO> riskparams;
    private Set<VehicleDTO> vehicles;
}
