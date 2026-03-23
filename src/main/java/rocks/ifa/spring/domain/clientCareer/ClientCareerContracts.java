package rocks.ifa.spring.domain.clientCareer;

import java.math.BigDecimal;

public interface ClientCareerContracts {

    record CareerRes(
        BigDecimal baseSalary,
        BigDecimal otherAllowance,
        BigDecimal laborInsurance,
        BigDecimal healthInsurance,
        BigDecimal otherDeduction,
        BigDecimal pensionPersonalRate,
        BigDecimal pensionPersonalAmount,
        BigDecimal pensionEmployerAmount,
        BigDecimal pensionTotalAmount,
        BigDecimal stockDeduction,
        BigDecimal stockCompanyMatch,
        BigDecimal monthlyNetIncome,
        BigDecimal annualBonus,
        BigDecimal annualTotalIncome,
        Integer dependents
    ) {}

    record UpdateCareerReq(
        BigDecimal baseSalary,
        BigDecimal otherAllowance,
        BigDecimal laborInsurance,
        BigDecimal healthInsurance,
        BigDecimal otherDeduction,
        BigDecimal pensionPersonalRate,
        BigDecimal stockDeduction,
        BigDecimal stockCompanyMatch,
        BigDecimal annualBonus,
        Integer dependents
    ) {}
}
