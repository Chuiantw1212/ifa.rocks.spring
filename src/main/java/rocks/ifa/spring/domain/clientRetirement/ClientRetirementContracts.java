package rocks.ifa.spring.domain.clientRetirement;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface ClientRetirementContracts {

    @Schema(description = "客戶的退休規劃資料回應")
    record RetirementRes(
        @Schema(description = "系統內部 ID")
        Long id,
        @Schema(description = "最後更新時間")
        OffsetDateTime updatedAt,
        
        // Go-Go Phase
        @Schema(description = "家庭型態")
        String householdType,
        @Schema(description = "居住方式")
        String housingMode,
        @Schema(description = "居住花費")
        BigDecimal housingCost,
        @Schema(description = "健康等級代碼")
        String healthTierCode,
        @Schema(description = "健康花費")
        BigDecimal healthCost,
        @Schema(description = "活躍生活代碼")
        String activeLivingCode,
        @Schema(description = "活躍生活花費")
        BigDecimal activeLivingCost,

        // Slow-Go Phase
        @Schema(description = "放緩期開始年齡")
        Integer slowGoStartAge,
        @Schema(description = "防禦機制代碼")
        String defenseTierCode,
        @Schema(description = "每月醫療花費")
        BigDecimal monthlyMedicalCost,
        @Schema(description = "重大疾病代碼")
        String criticalIllnessCode,
        @Schema(description = "重大疾病準備金")
        BigDecimal criticalIllnessReserve,

        // No-Go Phase
        @Schema(description = "長照期開始年齡")
        Integer nogoStartAge,
        @Schema(description = "長照模式")
        String ltcCareMode,
        @Schema(description = "長照每月花費")
        BigDecimal ltcMonthlyCost,
        @Schema(description = "長照每月耗材花費")
        BigDecimal ltcMonthlySupplies,
        @Schema(description = "長照補助")
        BigDecimal ltcSubsidy
    ) {}
}
