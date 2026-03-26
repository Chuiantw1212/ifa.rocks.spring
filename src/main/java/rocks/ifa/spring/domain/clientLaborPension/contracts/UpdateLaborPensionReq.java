package rocks.ifa.spring.domain.clientLaborPension.contracts;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "用於更新客戶勞工退休金資料的請求")
public record UpdateLaborPensionReq(
    @Schema(description = "預計退休年齡", example = "65")
    Integer expectedRetirementAge,
    @Schema(description = "預估退休金投資報酬率 (%)", example = "5.0")
    BigDecimal retirementRoi,
    @Schema(description = "雇主提繳累計金額")
    BigDecimal employerContribution,
    @Schema(description = "雇主提繳收益累計")
    BigDecimal employerEarnings,
    @Schema(description = "個人提繳累計金額")
    BigDecimal personalContribution,
    @Schema(description = "個人提繳收益累計")
    BigDecimal personalEarnings,
    @Schema(description = "目前已累積工作年資 (年)")
    BigDecimal currentWorkSeniority
) {}
