package rocks.ifa.spring.domain.clientCareer;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import rocks.ifa.spring.domain.common.ClientBaseEntity;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client_careers")
public class ClientCareerEntity extends ClientBaseEntity {

    // The 'id' and 'agentFirebaseUid' fields are inherited from ClientBaseEntity.
    // The 'id' now acts as the foreign key to client_profiles.

    private BigDecimal baseSalary;
    private BigDecimal otherAllowance;
    private BigDecimal annualBonus;
    
    private BigDecimal laborInsurance;
    private BigDecimal healthInsurance;
    private BigDecimal otherDeduction;
    
    private BigDecimal pensionPersonalRate;
    private BigDecimal pensionPersonalAmount;
    private BigDecimal pensionEmployerAmount;
    private BigDecimal pensionTotalAmount;
    
    private BigDecimal stockDeduction;
    private BigDecimal stockCompanyMatch;
    
    private BigDecimal monthlyNetIncome;
    private BigDecimal annualTotalIncome;

    private Integer dependents;
}
