package rocks.ifa.spring.domain.clientCareer;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserCareerRes {
    private BigDecimal baseSalary;
    private BigDecimal otherAllowance;
    private BigDecimal laborInsurance;
    private BigDecimal healthInsurance;
    private BigDecimal otherDeduction;
    private BigDecimal pensionPersonalRate;
    private BigDecimal pensionPersonalAmount;
    private BigDecimal pensionEmployerAmount;
    private BigDecimal pensionTotalAmount;
    private BigDecimal stockDeduction;
    private BigDecimal stockCompanyMatch;
    private BigDecimal monthlyNetIncome;
    private BigDecimal annualBonus;
    private BigDecimal annualTotalIncome;
    private Integer dependents;
}
