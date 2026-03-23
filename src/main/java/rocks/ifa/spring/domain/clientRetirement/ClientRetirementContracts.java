package rocks.ifa.spring.domain.clientRetirement;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface ClientRetirementContracts {

    record RetirementRes(
        Long id,
        OffsetDateTime updatedAt,
        String householdType,
        String housingMode,
        BigDecimal housingCost,
        String healthTierCode,
        BigDecimal healthCost,
        String activeLivingCode,
        BigDecimal activeLivingCost,
        Integer slowGoStartAge,
        String defenseTierCode,
        BigDecimal monthlyMedicalCost,
        String criticalIllnessCode,
        BigDecimal criticalIllnessReserve,
        Integer nogoStartAge,
        String ltcCareMode,
        BigDecimal ltcMonthlyCost,
        BigDecimal ltcMonthlySupplies,
        BigDecimal ltcSubsidy
    ) {}
}
