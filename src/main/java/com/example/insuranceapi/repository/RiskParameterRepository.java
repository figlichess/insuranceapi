package com.example.insuranceapi.repository;

import com.example.insuranceapi.model.RiskParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskParameterRepository extends JpaRepository<RiskParameter, Integer> {
    Optional<RiskParameter> findByTypeAllIgnoreCase(String type);
}
