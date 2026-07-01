package rocks.ifa.spring.domain.clientLaborPension.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "用於更新客戶勞工退休金資料的請求")
public record UpdateLaborPensionReq(
    @Schema(description = "預計退休年齡") Integer expectedRetirementAge,
    @Schema(description = "退休後預期餘命") Integer remainingLifeAtRetirement,
    @Schema(description = "退休金投資報酬率預估 (%)") BigDecimal retirementRoi,
    @Schema(description = "雇主提繳累積額") BigDecimal employerContribution,
    @Schema(description = "雇主提繳收益") BigDecimal employerEarnings,
    @Schema(description = "個人提繳累積額") BigDecimal personalContribution,
    @Schema(description = "個人提繳收益") BigDecimal personalEarnings,
    @Schema(description = "目前年資 (月)") Integer currentWorkSeniority
) {}
