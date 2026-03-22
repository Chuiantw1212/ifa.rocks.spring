package com.en_chu.calculator_api_spring.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserBusiness extends UserBaseEntity {

	private String name;

	private LocalDate startDate;

	/**
	 * 專案年期 (例如：5年、10年)
	 */
	private Integer projectYears;

	// deemed_6 | verified | exempt
	private String taxCategory;

	private BigDecimal acquisitionCost;

	// monthly | total
	private String incomeMode;

	private BigDecimal totalAccumulatedIncome;

	private BigDecimal monthlyIncome;

	private BigDecimal monthlyCost;

	private BigDecimal loanAmount;

	private BigDecimal loanInterestRate;
	/**
	 * 投資報酬率 (ROI) 存字串，例如: "25.5%"
	 */
	private String roi;

	/**
	 * 內部報酬率 (IRR) 存字串，例如: "12.8%" 或 "無法計算"
	 */
	private String irr;
	
	/**
     * 分組 ID (1~5)
     * 對應前端的顏色標籤設定
     */
    private Integer groupId;
}