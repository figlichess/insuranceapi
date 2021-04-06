package com.example.insuranceapi.service;

import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.RiskParameter;
import com.example.insuranceapi.repository.RiskParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RiskParameterServiceImpl implements RiskParameterService {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Autowired
    private RiskParameterRepository riskParameterRepository;

    @Override
    public RiskParameter saveRiskParameter(RiskParameter riskParameter) {
        return riskParameterRepository.save(riskParameter);
    }

    @Override
    public List<RiskParameter> getRiskParameters() {
        return riskParameterRepository.findAll();
    }

    @Override
    public void deleteRiskParameter(Integer id) throws ResourceNotFoundException {
        RiskParameter riskParameter = getRiskParameter(id);
        riskParameterRepository.delete(riskParameter);
    }

    @Override
    public RiskParameter updateRiskParameter(Integer id, RiskParameter riskParameterData) throws ResourceNotFoundException {
        RiskParameter riskParameter = getRiskParameter(id);
        riskParameter.setValue(riskParameterData.getValue());
        riskParameter.setType(riskParameterData.getType());
        riskParameter.setVehicleParamType(riskParameterData.getVehicleParamType());
        final RiskParameter updatedRiskParameter = riskParameterRepository.save(riskParameter);
        return updatedRiskParameter;
    }

    @Override
    public RiskParameter getRiskParameter(Integer id) throws ResourceNotFoundException {
        return riskParameterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("RiskParameter not found for this id :: " + id));
    }

    @Override
    public void batchRisks(Set<RiskParameter> riskParameters) {
        Set<RiskParameter> riskParametersToBatch = new HashSet<>();

        for (RiskParameter riskParameter : riskParameters) {
            riskParametersToBatch.add(riskParameter);

            if (riskParametersToBatch.size() % batchSize == 0 && riskParametersToBatch.size() > 0) {
                riskParameterRepository.saveAll(riskParametersToBatch);
                riskParametersToBatch.clear();
            }
        }

        if (riskParametersToBatch.size() > 0) {
            riskParameterRepository.saveAll(riskParametersToBatch);
            riskParametersToBatch.clear();
        }
    }

    @Override
    public Page<RiskParameter> getRiskParameterPages(PageRequest pageRequest) {
        return riskParameterRepository.findAll(pageRequest);
    }
}
