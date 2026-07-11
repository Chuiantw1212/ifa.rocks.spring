package rocks.ifa.spring.application.clientTax.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "用於更新客戶稅務資料的請求")
public record UpdateTaxReq(
    @Schema(description = "預估其他所得 (股利、利息、租金等)", example = "50000")
    BigDecimal estimatedOtherIncome
) {}
