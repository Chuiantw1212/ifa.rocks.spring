package rocks.ifa.spring.domain.clientTax.contracts;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "客戶的稅務資料回應")
public record TaxRes(
    @Schema(description = "預估其他所得 (股利、利息、租金等)", example = "50000")
    BigDecimal estimatedOtherIncome
) {}
