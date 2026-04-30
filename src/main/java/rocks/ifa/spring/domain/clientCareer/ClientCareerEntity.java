package rocks.ifa.spring.domain.clientCareer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import rocks.ifa.spring.domain.common.ClientBaseEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client_careers") // Corrected table name
public class ClientCareerEntity extends ClientBaseEntity {

    @Column(name = "client_id", nullable = false, unique = true)
    private UUID clientId;

    private BigDecimal baseSalary;
    private BigDecimal otherAllowance;
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
    private BigDecimal annualBonus;
    private BigDecimal annualTotalIncome;
    private Integer dependents;
}
