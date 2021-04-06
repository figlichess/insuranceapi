package com.example.insuranceapi.model;

import java.util.HashSet;
import java.util.Set;

public class CascoDataSet {
    public Set<RiskParameter> riskparameters;
    public Set<AdditionalRiskParameter> additionalRiskparameters;
    public Set<Vehicle> vehicles;

    public CascoDataSet() {
        this.riskparameters = new HashSet<>();
        this.additionalRiskparameters = new HashSet<>();
        this.vehicles = new HashSet<>();
    }
}
