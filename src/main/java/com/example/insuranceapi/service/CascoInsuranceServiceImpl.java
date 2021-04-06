package com.example.insuranceapi.service;

import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.CascoInsurance;
import com.example.insuranceapi.repository.CascoInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CascoInsuranceServiceImpl implements CascoInsuranceService {

    @Autowired
    private CascoInsuranceRepository cascoInsuranceRepository;

    @Override
    public CascoInsurance saveCascoInsurance(CascoInsurance CascoInsurance) {
        return cascoInsuranceRepository.save(CascoInsurance);
    }

    @Override
    public List<CascoInsurance> getCascoInsurances() {
        return cascoInsuranceRepository.findAll();
    }

    @Override
    public void deleteCascoInsurance(Integer id) throws ResourceNotFoundException {
        CascoInsurance CascoInsurance = getCascoInsurance(id);
        cascoInsuranceRepository.delete(CascoInsurance);
    }

    @Override
    public CascoInsurance updateCascoInsurance(Integer id, CascoInsurance CascoInsuranceData) throws ResourceNotFoundException {
        CascoInsurance CascoInsurance = getCascoInsurance(id);
        CascoInsurance.setAnnualPayment(CascoInsuranceData.getAnnualPayment());
        CascoInsurance.setRiskparameters(CascoInsuranceData.getRiskparameters());
        final CascoInsurance updatedCascoInsurance = cascoInsuranceRepository.save(CascoInsurance);
        return updatedCascoInsurance;
    }

    @Override
    public CascoInsurance getCascoInsurance(Integer id) throws ResourceNotFoundException {
        return cascoInsuranceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CascoInsurance not found for this id :: " + id));
    }

    public void removeRisks(CascoInsurance cascoInsurance) {
        cascoInsurance.getRiskparameters().removeAll(cascoInsurance.getRiskparameters());
    }
}
