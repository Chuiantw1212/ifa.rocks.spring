package rocks.ifa.spring.domain.clientLaborInsurance;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserLaborInsuranceRes {
    private Integer expectedClaimAge;
    private BigDecimal averageMonthlySalary;
    private Integer insuranceSeniority;
    private BigDecimal predictedRemainingLife;
    private BigDecimal predictedMonthlyAnnuity;
}
