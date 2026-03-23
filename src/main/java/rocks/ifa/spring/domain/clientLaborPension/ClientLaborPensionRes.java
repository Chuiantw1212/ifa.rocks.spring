package rocks.ifa.spring.domain.clientLaborPension;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ClientLaborPensionRes {
    private Integer expectedRetirementAge;
    private BigDecimal remainingLifeAtRetirement;
    private BigDecimal retirementRoi;
    private BigDecimal employerContribution;
    private BigDecimal employerEarnings;
    private BigDecimal personalContribution;
    private BigDecimal personalEarnings;
    private BigDecimal currentWorkSeniority;
    private BigDecimal predictedLumpSum;
    private BigDecimal predictedNetLumpSum;
}
