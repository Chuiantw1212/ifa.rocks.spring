package com.en_chu.calculator_api_spring.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "使用者勞工退休金規劃參數")
public class UserLaborPensionDto extends BaseDto {

	// ==========================================
	// 1. 退休參數設定
	// ==========================================

	@NotNull(message = "預計退休年齡不能為空")
	@Min(value = 0, message = "年齡不能為負數")
	@Schema(description = "預計退休年齡", example = "65")
	private Integer expectedRetirementAge;

	@Schema(description = "退休時預估餘命 (年)", example = "20.5")
	@Min(value = 0, message = "餘命不能為負數")
	private BigDecimal remainingLifeAtRetirement;

	@Schema(description = "預估退休金投資報酬率 (%)", example = "5.0")
	private BigDecimal retirementRoi;

	// ==========================================
	// 2. 勞工退休金專戶累計
	// ==========================================

	@Schema(description = "雇主提繳累計金額", example = "500000")
	@Min(value = 0, message = "金額不能為負數")
	private BigDecimal employerContribution;

	@Schema(description = "雇主提繳收益累計", example = "50000")
	private BigDecimal employerEarnings;

	@Schema(description = "個人提繳累計金額", example = "300000")
	@Min(value = 0, message = "金額不能為負數")
	private BigDecimal personalContribution;

	@Schema(description = "個人提繳收益累計", example = "30000")
	private BigDecimal personalEarnings;

	// ==========================================
	// 3. 年資資料
	// ==========================================

	@Schema(description = "目前已累積工作年資 (年)", example = "10.5")
	@Min(value = 0, message = "年資不能為負數")
	private BigDecimal currentWorkSeniority;
	
	@Schema(description = "預估退休時累積總額 (稅前 FV)", example = "2500000")
    @Min(value = 0)
    private BigDecimal predictedLumpSum;

    @Schema(description = "預估稅後實領淨額 (Net FV)", example = "2450000")
    @Min(value = 0)
    private BigDecimal predictedNetLumpSum;
}