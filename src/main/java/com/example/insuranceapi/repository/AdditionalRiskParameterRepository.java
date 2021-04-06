package com.example.insuranceapi.repository;

import com.example.insuranceapi.model.AdditionalRiskParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdditionalRiskParameterRepository extends JpaRepository<AdditionalRiskParameter, Integer> {
    Optional<AdditionalRiskParameter> findByTypeAndProducerAllIgnoreCase(String type, String producer);
}
