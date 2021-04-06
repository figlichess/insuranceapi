package com.example.insuranceapi.service;

import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.CascoInsurance;

import java.util.List;

public interface CascoInsuranceService {
    public CascoInsurance saveCascoInsurance(CascoInsurance cascoInsurance);

    public List<CascoInsurance> getCascoInsurances();

    public void deleteCascoInsurance(Integer id) throws ResourceNotFoundException;

    public CascoInsurance getCascoInsurance(Integer id) throws ResourceNotFoundException;

    public CascoInsurance updateCascoInsurance(Integer id, CascoInsurance CascoInsurance) throws ResourceNotFoundException;

    public void removeRisks(CascoInsurance cascoInsurance);
}
