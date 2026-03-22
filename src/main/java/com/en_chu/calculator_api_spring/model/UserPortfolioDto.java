package com.en_chu.calculator_api_spring.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPortfolioDto  extends BaseDto {	
	private String countryCode; // e.g., US
	private String currency; // e.g., USD
	private BigDecimal exchangeRate;
	private BigDecimal marketValue;
	private BigDecimal realizedPnl;
}