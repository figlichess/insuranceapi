package com.example.insuranceapi.service;

import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.RiskParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

public interface RiskParameterService {
    public RiskParameter saveRiskParameter(RiskParameter riskParameter);

    public List<RiskParameter> getRiskParameters();

    public Page<RiskParameter> getRiskParameterPages(PageRequest pageRequest);

    public void deleteRiskParameter(Integer id) throws ResourceNotFoundException;

    public RiskParameter getRiskParameter(Integer id) throws ResourceNotFoundException;

    public RiskParameter updateRiskParameter(Integer id, RiskParameter riskParameter) throws ResourceNotFoundException;

    public void batchRisks(Set<RiskParameter> riskParameters);

}
