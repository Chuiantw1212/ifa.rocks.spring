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
@EqualsAndHashCode(callSuper = true) // 比較物件時包含父類別欄位 (ID, UID)
public class UserLaborInsurance extends UserBaseEntity {

	// ==========================================
	// 父類別已包含: id, firebaseUid, createdAt, updatedAt
	// 這裡只保留獨有的欄位
	// ==========================================

	/**
	 * 預計開始請領年齡
	 */
	private Integer expectedClaimAge;

	/**
	 * 最高 60 個月之平均投保薪資
	 */
	private BigDecimal averageMonthlySalary;

	/**
	 * 保險年資 (單位: 月)
	 */
	private Integer insuranceSeniority;
	
	/**
     * 預估餘命 (年)
     */
    private BigDecimal predictedRemainingLife;
    
    /**
     * 預估每月領取金額 (勞保年金 Annuity)
     */
    private BigDecimal predictedMonthlyAnnuity;
}