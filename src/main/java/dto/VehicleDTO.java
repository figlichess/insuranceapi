package dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class VehicleDTO {
    private int id;
    private String plateNumber;
    private int firstRegistration;
    private long purchasePrice;
    private String producer;
    private int mileage;
    private double previousIndemnity;
    private double cascoAnnualPayment;
    private Set<String> riskParams;
}
