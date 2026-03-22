package com.en_chu.calculator_api_spring.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO for creating or fully updating user retirement settings.
 */
@Data
public class UserRetirementUpdateReq {

    @NotNull(message = "Household type cannot be null")
    private String householdType;

    private String housingMode;
    private BigDecimal housingCost;
    private String healthTierCode;
    private BigDecimal healthCost;
    private String activeLivingCode;
    private BigDecimal activeLivingCost;

    private Integer slowGoStartAge;
    private String defenseTierCode;
    private BigDecimal monthlyMedicalCost;
    private String criticalIllnessCode;
    private BigDecimal criticalIllnessReserve;

    private Integer nogoStartAge;
    private String ltcCareMode;
    private BigDecimal ltcMonthlyCost;
    private BigDecimal ltcMonthlySupplies;
    private BigDecimal ltcSubsidy;
}
