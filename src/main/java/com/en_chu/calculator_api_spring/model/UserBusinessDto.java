package com.en_chu.calculator_api_spring.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserBusinessDto extends BaseDto {

	// ==========================================
	// 1. 基本資料 (必填)
	// ==========================================
	@Schema(description = "事業名稱", example = "網拍副業", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "事業名稱不能為空")
	private String name;

	@Schema(description = "開始經營日期 (YYYY-MM-DD)", example = "2023-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "開始經營日期必填")
	@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = com.en_chu.calculator_api_spring.util.FlexibleDateDeserializer.class)
	private LocalDate startDate;

	/**
	 * 專案年期 用於定義 IRR 計算的現金流陣列長度 (可選，null 代表永續)
	 */
	@Schema(description = "專案年期 (用於計算 IRR)", example = "5")
	@Min(value = 1, message = "若填寫年期，至少需為 1 年")
	private Integer projectYears;

	@Schema(description = "稅務類別", example = "exempt", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "稅務類別必填")
	@Pattern(regexp = "^(deemed_6|verified|exempt)$", message = "無效的稅務類別")
	private String taxCategory;

	@Schema(description = "初始投入成本 (開辦費)", example = "50000")
	@NotNull(message = "初始成本必填")
	@DecimalMin(value = "0.0", message = "成本不能小於 0")
	private BigDecimal acquisitionCost;

	// ==========================================
	// 2. 收入模式與數值 (必填)
	// ==========================================

	@Schema(description = "收入輸入模式 (monthly/total)", example = "monthly", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "收入模式必填")
	@Pattern(regexp = "^(monthly|total)$", message = "無效的收入模式")
	private String incomeMode;

	@Schema(description = "月平均收入 (系統計算核心)", example = "15000")
	@NotNull(message = "月平均收入必填")
	@DecimalMin(value = "0.0", message = "月收入不能小於 0")
	private BigDecimal monthlyIncome;

	@Schema(description = "月平均成本/支出", example = "3000")
	@NotNull(message = "月平均成本必填")
	@DecimalMin(value = "0.0", message = "月成本不能小於 0")
	private BigDecimal monthlyCost;

	@Schema(description = "歷史累計總營收 (當 mode=total 時使用)", example = "0")
	@DecimalMin(value = "0.0", message = "累計營收不能小於 0")
	private BigDecimal totalAccumulatedIncome = BigDecimal.ZERO;

	// ==========================================
	// 3. 貸款資訊 (必填)
	// ==========================================

	@Schema(description = "創業貸款餘額", example = "100000")
	@NotNull(message = "貸款餘額必填 (無貸款請傳 0)")
	@DecimalMin(value = "0.0", message = "貸款不能小於 0")
	private BigDecimal loanAmount;

	@Schema(description = "貸款年利率 (%)", example = "2.5")
	@NotNull(message = "貸款利率必填 (無貸款請傳 0)")
	@DecimalMin(value = "0.0", message = "利率不能小於 0")
	private BigDecimal loanInterestRate;

	// ==========================================
	// 4. 財務指標 (新增 - 前端計算結果)
	// ==========================================

	@Schema(description = "投資報酬率 (ROI) - 顯示用字串", example = "25.5%")
	private String roi;

	@Schema(description = "內部報酬率 (IRR) - 顯示用字串", example = "12.8%")
	private String irr;
	
	@Schema(description = "分組標籤 ID (1:藍, 2:橘, 3:紫, 4:青, 5:粉)", example = "1")
    @Min(value = 1, message = "分組 ID 最小為 1")
    @Max(value = 5, message = "分組 ID 最大為 5")
    private Integer groupId;
}