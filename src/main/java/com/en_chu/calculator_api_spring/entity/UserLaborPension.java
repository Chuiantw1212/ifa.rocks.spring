package com.en_chu.calculator_api_spring.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // ✅ 務必加上：比對時包含父類別 ID
public class UserLaborPension extends UserBaseEntity {
	// --- 1. 退休參數 ---
	private Integer expectedRetirementAge;
	private BigDecimal remainingLifeAtRetirement;
	private BigDecimal retirementRoi;

	// --- 2. 勞工退休金專戶 ---
	private BigDecimal employerContribution;
	private BigDecimal employerEarnings;
	private BigDecimal personalContribution;
	private BigDecimal personalEarnings;

	// --- 3. 年資 ---
	private BigDecimal currentWorkSeniority;
	
	/**
     * 預估退休時累積總額 (稅前 FV)
     */
    private BigDecimal predictedLumpSum;

    /**
     * 預估稅後實領淨額 (Net FV)
     */
    private BigDecimal predictedNetLumpSum;
}