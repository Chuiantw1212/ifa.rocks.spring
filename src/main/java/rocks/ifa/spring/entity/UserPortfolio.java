package rocks.ifa.spring.entity;

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
public class UserPortfolio extends UserBaseEntity {
	private String countryCode;
	private String currency;
	private BigDecimal exchangeRate;
	private BigDecimal marketValue;
	private BigDecimal realizedPnl;
}
