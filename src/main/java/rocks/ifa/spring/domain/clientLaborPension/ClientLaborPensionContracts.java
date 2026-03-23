package rocks.ifa.spring.domain.clientLaborPension;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public interface ClientLaborPensionContracts {

    @Schema(description = "客戶的勞工退休金資料回應")
    record LaborPensionRes(
        @Schema(description = "預計退休年齡", example = "65")
        Integer expectedRetirementAge,
        @Schema(description = "退休時預估餘命 (年)", example = "20.5")
        BigDecimal remainingLifeAtRetirement,
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
        BigDecimal currentWorkSeniority,
        @Schema(description = "預估退休時累積總額 (稅前)")
        BigDecimal predictedLumpSum,
        @Schema(description = "預估稅後實領淨額")
        BigDecimal predictedNetLumpSum
    ) {}

    @Schema(description = "用於更新客戶勞工退休金資料的請求")
    record UpdateLaborPensionReq(
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
}
