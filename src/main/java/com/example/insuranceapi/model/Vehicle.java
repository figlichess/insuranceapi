package com.example.insuranceapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vehicle")
public class Vehicle implements InputOutputData, Serializable {
    private static final long serialVersionUID = 7229306081355175362L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String plateNumber;
    private int firstRegistration;
    private long purchasePrice;
    private String producer;
    private int mileage;
    private double previousIndemnity;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cascoinsurance_id", referencedColumnName = "id")
    private CascoInsurance cascoInsurance;

    public Vehicle(String plateNumber, int firstRegistration, long purchasePrice,
                   String producer, int mileage, double previousIndemnity) {
        this.plateNumber = plateNumber;
        this.firstRegistration = firstRegistration;
        this.purchasePrice = purchasePrice;
        this.producer = producer;
        this.mileage = mileage;
        this.previousIndemnity = previousIndemnity;
    }

    public String[] getDataForOutput() {
        return new String[]{plateNumber, producer, cascoInsurance != null ? String.valueOf(cascoInsurance.getAnnualPayment()) : ""};
    }
}
