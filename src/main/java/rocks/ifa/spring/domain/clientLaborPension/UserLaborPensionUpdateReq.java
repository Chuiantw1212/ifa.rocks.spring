package rocks.ifa.spring.domain.clientLaborPension;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserLaborPensionUpdateReq {
    private Integer expectedRetirementAge;
    private BigDecimal retirementRoi;
    private BigDecimal employerContribution;
    private BigDecimal employerEarnings;
    private BigDecimal personalContribution;
    private BigDecimal personalEarnings;
    private BigDecimal currentWorkSeniority;
}
