package rocks.ifa.spring.domain.clientRetirement;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import rocks.ifa.spring.domain.entityBase.ClientBaseEntity;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_retirements")
public class ClientRetirementEntity extends ClientBaseEntity {
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
