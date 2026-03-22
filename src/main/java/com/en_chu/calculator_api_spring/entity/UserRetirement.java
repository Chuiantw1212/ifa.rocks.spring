package com.en_chu.calculator_api_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Represents the user's retirement lifecycle settings.
 * Aligned with the frontend UserRetirement interface.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRetirement extends UserBaseEntity {

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
