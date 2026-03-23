package rocks.ifa.spring.domain.clientLaborInsurance;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public interface ClientLaborInsuranceContracts {

    @Schema(description = "客戶的勞工保險資料回應")
    record LaborInsuranceRes(
        @Schema(description = "預計開始請領年齡", example = "60")
        Integer expectedClaimAge,
        @Schema(description = "最高 60 個月之平均投保薪資", example = "45800")
        BigDecimal averageMonthlySalary,
        @Schema(description = "保險年資 (總月數)", example = "180")
        Integer insuranceSeniority,
        @Schema(description = "請領時預估餘命 (年)", example = "20.5")
        BigDecimal predictedRemainingLife,
        @Schema(description = "預估每月領取金額 (勞保年金)")
        BigDecimal predictedMonthlyAnnuity
    ) {}

    @Schema(description = "用於更新客戶勞工保險資料的請求")
    record UpdateLaborInsuranceReq(
        @Schema(description = "預計開始請領年齡", example = "60")
        Integer expectedClaimAge,
        @Schema(description = "最高 60 個月之平均投保薪資", example = "45800")
        BigDecimal averageMonthlySalary,
        @Schema(description = "保險年資 (總月數)", example = "180")
        Integer insuranceSeniority,
        @Schema(description = "請領時預估餘命 (年)", example = "20.5")
        BigDecimal predictedRemainingLife,
        @Schema(description = "預估每月領取金額 (勞保年金)")
        BigDecimal predictedMonthlyAnnuity
    ) {}
}
