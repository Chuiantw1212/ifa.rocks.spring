package rocks.ifa.spring.domain.clientLaborPension;

import java.math.BigDecimal;

public interface ClientLaborPensionContracts {

    record LaborPensionRes(
        Integer expectedRetirementAge,
        BigDecimal remainingLifeAtRetirement,
        BigDecimal retirementRoi,
        BigDecimal employerContribution,
        BigDecimal employerEarnings,
        BigDecimal personalContribution,
        BigDecimal personalEarnings,
        BigDecimal currentWorkSeniority,
        BigDecimal predictedLumpSum,
        BigDecimal predictedNetLumpSum
    ) {}

    record UpdateLaborPensionReq(
        Integer expectedRetirementAge,
        BigDecimal retirementRoi,
        BigDecimal employerContribution,
        BigDecimal employerEarnings,
        BigDecimal personalContribution,
        BigDecimal personalEarnings,
        BigDecimal currentWorkSeniority
    ) {}
}
