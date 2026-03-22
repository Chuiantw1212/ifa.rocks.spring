package com.en_chu.calculator_api_spring.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder; // 注意這裡的 import

@Data
@SuperBuilder // 1. 改用 SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // 2. 比較時包含父類別
public class UserCareer extends UserBaseEntity { // 3. 繼承父類別
	// 薪資結構
	private BigDecimal baseSalary;
	private BigDecimal otherAllowance;
	private BigDecimal laborInsurance;
	private BigDecimal healthInsurance;
	private BigDecimal otherDeduction;

	// 退休金
	private BigDecimal pensionPersonalRate;
	private BigDecimal pensionPersonalAmount;

	// 股票/福利
	private BigDecimal stockDeduction;
	private BigDecimal stockCompanyMatch;
	
	// 每個月實領金額
	private BigDecimal monthlyNetIncome;
	
	// 新增：雇主提撥金額
    private BigDecimal pensionEmployerAmount;

    // 新增：總提撥金額 (個人 + 雇主)
    private BigDecimal pensionTotalAmount;
    
    /**
     * 年終與非經常性獎金
     */
    private BigDecimal annualBonus;

    /**
     * 全年總薪資 (月薪*12 + 獎金，或使用者自行輸入)
     */
    private BigDecimal annualTotalIncome;
    
    /**
     * 健保扶養人數 (0~3，超過3人以3人計)
     */
    private Integer dependents;
}