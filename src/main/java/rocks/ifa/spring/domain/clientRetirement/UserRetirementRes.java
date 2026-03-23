package rocks.ifa.spring.domain.clientRetirement;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class UserRetirementRes {
    private Long id;
    private OffsetDateTime updatedAt;
    private String householdType;
    private String housingMode;
    private BigDecimal housingCost;
    private String healthTierCode;
    private BigDecimal healthCost;
    private String activeLivingCode;
    private BigDecimal activeLivingCost;
    private Integer slowGoStartAge;
    private String defenseTierCode;
    private BigDecimal monthlyMedicalCost;
    private String criticalIllnessCode;
    private BigDecimal criticalIllnessReserve;
    private Integer nogoStartAge;
    private String ltcCareMode;
    private BigDecimal ltcMonthlyCost;
    private BigDecimal ltcMonthlySupplies;
    private BigDecimal ltcSubsidy;
}
