package rocks.ifa.spring.application.clientLaborPension.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "客戶的勞工退休金資料回應")
public record LaborPensionRes(
    @Schema(description = "記錄的唯一識別碼") UUID id,
    @Schema(description = "預計退休年齡") Integer expectedRetirementAge,
    @Schema(description = "退休後預期餘命") Integer remainingLifeAtRetirement,
    @Schema(description = "退休金投資報酬率預估 (%)") BigDecimal retirementRoi,
    @Schema(description = "雇主提繳累積額") BigDecimal employerContribution,
    @Schema(description = "雇主提繳收益") BigDecimal employerEarnings,
    @Schema(description = "個人提繳累積額") BigDecimal personalContribution,
    @Schema(description = "個人提繳收益") BigDecimal personalEarnings,
    @Schema(description = "目前年資 (月)") Integer currentWorkSeniority,
    @Schema(description = "預估退休時累積總額 (稅前 FV)", accessMode = Schema.AccessMode.READ_ONLY) BigDecimal predictedLumpSum,
    @Schema(description = "預估稅後實領淨額 (Net FV)", accessMode = Schema.AccessMode.READ_ONLY) BigDecimal predictedNetLumpSum
) {}
