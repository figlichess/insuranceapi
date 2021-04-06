package com.example.insuranceapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "riskparameter")
public class RiskParameter implements Serializable {
    private static final long serialVersionUID = 3006660381196532833L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    private String vehicleParamType;
    private double value;
    private boolean fromFormula;
    @ManyToMany(mappedBy = "riskparameters", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<CascoInsurance> cascoinsurances = new HashSet<>();

    public RiskParameter(String type, String vehicleParamType, double value, boolean fromFormula) {
        this.vehicleParamType = vehicleParamType;
        this.type = type;
        this.value = value;
        this.fromFormula = fromFormula;
    }

}
