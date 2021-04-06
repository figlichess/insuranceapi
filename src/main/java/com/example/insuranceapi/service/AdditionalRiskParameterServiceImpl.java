package com.example.insuranceapi.service;

import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.AdditionalRiskParameter;
import com.example.insuranceapi.repository.AdditionalRiskParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AdditionalRiskParameterServiceImpl implements AdditionalRiskParameterService {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Autowired
    private AdditionalRiskParameterRepository addRiskParameterRepository;

    @Override
    public AdditionalRiskParameter saveAdditionalRiskParameter(AdditionalRiskParameter addRiskParam) {
        return addRiskParameterRepository.save(addRiskParam);
    }

    @Override
    public List<AdditionalRiskParameter> getAdditionalRiskParameters() {
        return addRiskParameterRepository.findAll();
    }

    @Override
    public void deleteAdditionalRiskParameter(Integer id) throws ResourceNotFoundException {
        AdditionalRiskParameter addRiskParam = getAdditionalRiskParameter(id);
        addRiskParameterRepository.delete(addRiskParam);
    }

    @Override
    public AdditionalRiskParameter updateAdditionalRiskParameter(Integer id, AdditionalRiskParameter addRiskParameterData) throws ResourceNotFoundException {
        AdditionalRiskParameter addRiskParam = getAdditionalRiskParameter(id);
        addRiskParam.setValue(addRiskParameterData.getValue());
        addRiskParam.setType(addRiskParameterData.getType());
        addRiskParam.setProducer(addRiskParameterData.getProducer());
        final AdditionalRiskParameter updatedRiskParameter = addRiskParameterRepository.save(addRiskParam);
        return updatedRiskParameter;
    }


    @Override
    public AdditionalRiskParameter getAdditionalRiskParameter(Integer id) throws ResourceNotFoundException {
        return addRiskParameterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AdditionalRiskParam not found for this id :: " + id));
    }

    @Override
    @Cacheable("additionalRiskParameterByTypeProducer")
    public AdditionalRiskParameter findAdditionalRiskParameterByTypeProducer(String type, String producer) throws ResourceNotFoundException {
        AdditionalRiskParameter additionalRiskParam;
        try {
            additionalRiskParam =
                    addRiskParameterRepository.findByTypeAndProducerAllIgnoreCase(type, producer).orElseThrow(() ->
                            new ResourceNotFoundException("AdditionalRiskParam not found for this type :: " + type + " and producer " + producer));
        } catch (ResourceNotFoundException ex) {
            if (type.equals("make_coefficients")) {
                additionalRiskParam = new AdditionalRiskParameter("make_coefficient", null, 1);
            } else if (type.equals("avg_purchase_price")) {
                additionalRiskParam = null;
            } else {
                throw ex;
            }
        }
        return additionalRiskParam;
    }

    @Override
    @Cacheable("addRiskByTypeProducerFromSet")
    public AdditionalRiskParameter findAddRiskByTypeProducerFromSet(Set<AdditionalRiskParameter> AddRisks, String type, String producer) {
        return AddRisks.stream().
                filter(addriskparam -> type.equals(addriskparam.getType()) && producer.equalsIgnoreCase(addriskparam.getProducer())).findAny().orElse(null);
    }

    @Override
    public void batchAddRisks(Set<AdditionalRiskParameter> addRiskParameters) {
        Set<AdditionalRiskParameter> addRiskParametersToBatch = new HashSet<>();

        for (AdditionalRiskParameter addRiskParameter : addRiskParameters) {
            addRiskParametersToBatch.add(addRiskParameter);

            if (addRiskParametersToBatch.size() % batchSize == 0 && addRiskParametersToBatch.size() > 0) {
                addRiskParameterRepository.saveAll(addRiskParametersToBatch);
                addRiskParametersToBatch.clear();
            }
        }

        if (addRiskParametersToBatch.size() > 0) {
            addRiskParameterRepository.saveAll(addRiskParametersToBatch);
            addRiskParametersToBatch.clear();
        }
    }
}
