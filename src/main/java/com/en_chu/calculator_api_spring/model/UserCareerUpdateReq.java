package com.en_chu.calculator_api_spring.model;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 專門用於更新使用者職涯薪資結構的請求 DTO。
 */
@Data
public class UserCareerUpdateReq {

    // 薪資結構
    private BigDecimal baseSalary;
    private BigDecimal otherAllowance;
    private BigDecimal laborInsurance;
    private BigDecimal healthInsurance;
    private BigDecimal otherDeduction;

    // 退休金
    private BigDecimal pensionPersonalRate;

    // 股票/福利
    private BigDecimal stockDeduction;
    private BigDecimal stockCompanyMatch;

    // 年終與非經常性獎金
    private BigDecimal annualBonus;

    // 健保扶養人數
    private Integer dependents;
}
