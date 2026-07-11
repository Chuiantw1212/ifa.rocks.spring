package rocks.ifa.spring.application.clientLaborInsurance.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "用於更新客戶勞保老年年金資料的請求")
public record UpdateLaborInsuranceReq(
    @Schema(description = "預計請領年齡") Integer expectedClaimAge,
    @Schema(description = "最高 60 個月平均投保薪資") BigDecimal averageMonthlySalary,
    @Schema(description = "保險年資 (月)") Integer insuranceSeniority,
    @Schema(description = "預估領取年限") Integer predictedRemainingLife
) {}
