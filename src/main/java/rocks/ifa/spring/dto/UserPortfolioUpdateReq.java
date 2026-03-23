package rocks.ifa.spring.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 專門用於更新投資組合的請求 DTO。
 */
@Data
public class UserPortfolioUpdateReq {
    private String countryCode;
    private String currency;
    private BigDecimal exchangeRate;
    private BigDecimal marketValue;
    private BigDecimal realizedPnl;
}
