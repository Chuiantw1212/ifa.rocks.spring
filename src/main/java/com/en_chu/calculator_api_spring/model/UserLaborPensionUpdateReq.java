package com.en_chu.calculator_api_spring.model;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 專門用於更新使用者勞工退休金資料的請求 DTO。
 */
@Data
public class UserLaborPensionUpdateReq {

    // --- 1. 退休參數 ---
    private Integer expectedRetirementAge;
    private BigDecimal retirementRoi;

    // --- 2. 勞工退休金專戶 ---
    private BigDecimal employerContribution;
    private BigDecimal employerEarnings;
    private BigDecimal personalContribution;
    private BigDecimal personalEarnings;

    // --- 3. 年資 ---
    private BigDecimal currentWorkSeniority;
}
