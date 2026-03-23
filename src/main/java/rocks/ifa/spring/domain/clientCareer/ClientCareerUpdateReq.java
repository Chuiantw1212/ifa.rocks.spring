package rocks.ifa.spring.domain.clientCareer;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ClientCareerUpdateReq {
    private BigDecimal baseSalary;
    private BigDecimal otherAllowance;
    private BigDecimal laborInsurance;
    private BigDecimal healthInsurance;
    private BigDecimal otherDeduction;
    private BigDecimal pensionPersonalRate;
    private BigDecimal stockDeduction;
    private BigDecimal stockCompanyMatch;
    private BigDecimal annualBonus;
    private Integer dependents;
}
