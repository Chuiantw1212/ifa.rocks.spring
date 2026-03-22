package com.en_chu.calculator_api_spring.model;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 專門用於新增投資組合的請求 DTO。
 */
@Data
public class UserPortfolioCreateReq {
    private String countryCode;
    private String currency;
    private BigDecimal exchangeRate;
    private BigDecimal marketValue;
    // realizedPnl 通常在建立時為 0，可以在 Service 層設定預設值
}
