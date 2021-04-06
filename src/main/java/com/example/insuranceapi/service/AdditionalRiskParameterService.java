package com.example.insuranceapi.service;

import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.AdditionalRiskParameter;

import java.util.List;
import java.util.Set;

public interface AdditionalRiskParameterService {
    public AdditionalRiskParameter saveAdditionalRiskParameter(AdditionalRiskParameter riskParameter);

    public List<AdditionalRiskParameter> getAdditionalRiskParameters();

    public void deleteAdditionalRiskParameter(Integer id) throws ResourceNotFoundException;

    public AdditionalRiskParameter getAdditionalRiskParameter(Integer id) throws ResourceNotFoundException;

    public AdditionalRiskParameter findAdditionalRiskParameterByTypeProducer(String type, String producer) throws ResourceNotFoundException;

    public AdditionalRiskParameter findAddRiskByTypeProducerFromSet(Set<AdditionalRiskParameter> AddRisks, String type, String producer) throws ResourceNotFoundException;

    public AdditionalRiskParameter updateAdditionalRiskParameter(Integer id, AdditionalRiskParameter riskParameter) throws ResourceNotFoundException;

    public void batchAddRisks(Set<AdditionalRiskParameter> addRiskParameters);

}
