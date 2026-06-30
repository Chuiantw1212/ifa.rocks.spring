package rocks.ifa.spring.domain.clientLaborInsurance;

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
@Table(name = "client_labor_insurances")
@AttributeOverride(name = "id", column = @Column(name = "client_id"))
public class ClientLaborInsuranceEntity extends ClientBaseEntity {
    private Integer expectedClaimAge;
    private BigDecimal averageMonthlySalary;
    private Integer insuranceSeniority;
    private Integer predictedRemainingLife;
    private BigDecimal predictedMonthlyAnnuity;
}
