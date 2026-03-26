package rocks.ifa.spring.domain.clientCareer.contracts;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "用於更新客戶職涯資料的請求")
public record UpdateCareerReq(
    // ... all fields from the original record
) {}
