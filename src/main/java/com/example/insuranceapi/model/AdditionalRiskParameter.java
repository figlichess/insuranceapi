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
@Table(name = "additionalriskparameter")
public class AdditionalRiskParameter implements Serializable {

    private static final long serialVersionUID = 7122689790959953501L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    private String producer;
    private double value;

    public AdditionalRiskParameter(String producer, String type, double value) {
        this.producer = producer;
        this.type = type;
        this.value = value;
    }
}
