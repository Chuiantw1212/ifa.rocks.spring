package rocks.ifa.spring.domain.clientTax;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public interface ClientTaxContracts {

    @Schema(description = "客戶的稅務資料回應")
    record TaxRes(
        @Schema(description = "預估其他所得 (股利、利息、租金等)", example = "50000")
        BigDecimal estimatedOtherIncome
    ) {}

    @Schema(description = "用於更新客戶稅務資料的請求")
    record UpdateTaxReq(
        @Schema(description = "預估其他所得 (股利、利息、租金等)", example = "50000")
        BigDecimal estimatedOtherIncome
    ) {}
}
