package com.en_chu.calculator_api_spring.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * DTO for UserRetirement data.
 */
@Data
public class UserRetirementDto {

    private Long id;
    private OffsetDateTime updatedAt;

    // --- Phase 1: Go-Go ---
    private String householdType;
    private String housingMode;
    private BigDecimal housingCost;
    private String healthTierCode;
    private BigDecimal healthCost;
    private String activeLivingCode;
    private BigDecimal activeLivingCost;

    // --- Phase 2: Slow-Go ---
    private Integer slowGoStartAge;
    private String defenseTierCode;
    private BigDecimal monthlyMedicalCost;
    private String criticalIllnessCode;
    private BigDecimal criticalIllnessReserve;

    // --- Phase 3: No-Go ---
    private Integer nogoStartAge;
    private String ltcCareMode;
    private BigDecimal ltcMonthlyCost;
    private BigDecimal ltcMonthlySupplies;
    private BigDecimal ltcSubsidy;
}
