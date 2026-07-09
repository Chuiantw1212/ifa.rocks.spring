package rocks.ifa.spring.application.clientLaborInsurance.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "客戶勞保老年年金資料的回應")
public record LaborInsuranceRes(
    @Schema(description = "記錄的唯一識別碼") UUID clientId,
    @Schema(description = "預計請領年齡") Integer expectedClaimAge,
    @Schema(description = "最高 60 個月平均投保薪資") BigDecimal averageMonthlySalary,
    @Schema(description = "保險年資 (月)") Integer insuranceSeniority,
    @Schema(description = "預估領取年限") Integer predictedRemainingLife,
    @Schema(description = "預估每月領取金額 (Annuity)", accessMode = Schema.AccessMode.READ_ONLY) BigDecimal predictedMonthlyAnnuity
) {}
