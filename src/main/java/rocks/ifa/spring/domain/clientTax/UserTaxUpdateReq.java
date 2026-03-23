package rocks.ifa.spring.domain.clientTax;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserTaxUpdateReq {
    private BigDecimal estimatedOtherIncome;
}
