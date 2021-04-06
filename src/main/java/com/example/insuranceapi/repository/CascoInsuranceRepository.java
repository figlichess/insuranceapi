package com.example.insuranceapi.repository;

import com.example.insuranceapi.model.CascoInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CascoInsuranceRepository extends JpaRepository<CascoInsurance, Integer> {

}
