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
@EqualsAndHashCode(callSuper = true)
public class UserTax extends UserBaseEntity {

	/**
	 * 預估其他所得 (Other Income) 包含：股利、利息、租金、兼職等需併入綜所稅的金額
	 */
	private BigDecimal estimatedOtherIncome;

	// 未來擴充預留：
	// private BigDecimal overseasIncome;
	// private BigDecimal itemizedDeductions;
}