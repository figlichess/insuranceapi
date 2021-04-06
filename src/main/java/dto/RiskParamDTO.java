package dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RiskParamDTO {
    private int id;
    private String type;
    private String vehicleParamType;
    private double value;
    private boolean fromFormula;
    private boolean hasCasco;
}
