package rocks.ifa.spring.domain.clientLaborInsurance;

import java.math.BigDecimal;

public interface ClientLaborInsuranceContracts {

    record LaborInsuranceRes(
        Integer expectedClaimAge,
        BigDecimal averageMonthlySalary,
        Integer insuranceSeniority,
        BigDecimal predictedRemainingLife,
        BigDecimal predictedMonthlyAnnuity
    ) {}

    record UpdateLaborInsuranceReq(
        Integer expectedClaimAge,
        BigDecimal averageMonthlySalary,
        Integer insuranceSeniority,
        BigDecimal predictedRemainingLife,
        BigDecimal predictedMonthlyAnnuity
    ) {}
}
