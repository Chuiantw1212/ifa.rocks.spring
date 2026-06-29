package rocks.ifa.spring.domain.clientCareer;

import jakarta.persistence.AttributeOverride;
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

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client_careers")
@AttributeOverride(name = "id", column = @Column(name = "client_id"))
public class ClientCareerEntity extends ClientBaseEntity {

    // The inherited 'id' field is now mapped to the 'client_id' column in the database.
    // It still acts as the primary key and the foreign key.

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
