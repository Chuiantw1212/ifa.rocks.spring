package rocks.ifa.spring.domain.clientCareer.contracts;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "客戶的職涯與收入資料回應")
public record CareerRes(
    // ... all fields from the original record
) {}
