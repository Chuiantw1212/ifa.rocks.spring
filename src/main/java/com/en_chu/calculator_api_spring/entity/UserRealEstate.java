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
public class UserRealEstate extends UserBaseEntity {

	/**
	 * 物件名稱 例：板橋自用宅、信義區投資套房
	 */
	private String name;

	/**
	 * 屋齡 (年)
	 */
	private Integer age;

	/**
	 * 權狀坪數
	 */
	private BigDecimal size;

	/**
	 * 單價 (萬/坪)
	 */
	private BigDecimal pricePerPing;

	/**
	 * 總價 (市價) - 自動計算 Service 層寫入時建議計算： pricePerPing * size * 10000
	 */
	private BigDecimal totalPrice;

	/**
	 * 公告/評定現值 (稅基)
	 */
	private BigDecimal assessedValue;

	/**
	 * 預估持有稅率 (%)
	 */
	private BigDecimal holdingTaxRate;
	
    /**
     * [新增] 實際支付房屋稅 (年) - 用於核對與精準計算
     */
	private BigDecimal  actualHoldingCost; 

	/**
	 * 銀行貸款餘額
	 */
	private BigDecimal loanAmount;

	/**
	 * 年利率 (%)
	 */
	private BigDecimal interestRate;

	/**
	 * 用途狀態 self: 自用住宅 rent: 出租投資 vacant: 閒置資產 (建議在 DTO 層做 @Pattern 驗證，這裡存 String
	 * 即可)
	 */
	private String usageType;

	/**
	 * 月租金收入
	 */
	private BigDecimal monthlyRent;
}