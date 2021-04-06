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
@Table(name = "cascoinsurance")
public class CascoInsurance implements Serializable {
    private static final long serialVersionUID = 1727804772034838498L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double annualPayment;
    private double monthPayment;
    @OneToOne(mappedBy = "cascoInsurance", fetch = FetchType.LAZY)
    private Vehicle vehicle;
    @ManyToMany(fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinTable(name = "cascoinsurance_riskparameter",
            joinColumns = @JoinColumn(name = "cascoinsurance_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "riskparameter_id", referencedColumnName = "id"))
    private Set<RiskParameter> riskparameters = new HashSet<>();
}
