package rocks.ifa.spring.domain.clientLaborInsurance;

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
@Table(name = "user_labor_insurances")
public class ClientLaborInsuranceEntity extends ClientBaseEntity {
    private Integer expectedClaimAge;
    private BigDecimal averageMonthlySalary;
    private Integer insuranceSeniority;
    private BigDecimal predictedRemainingLife;
    private BigDecimal predictedMonthlyAnnuity;
}
