package rocks.ifa.spring.domain.clientLaborPension;

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
@Table(name = "client_labor_pensions")
@AttributeOverride(name = "id", column = @Column(name = "client_id"))
public class ClientLaborPensionEntity extends ClientBaseEntity {
    
    // The inherited 'id' field is now mapped to the 'client_id' column.

    private Integer expectedRetirementAge;
    private Integer remainingLifeAtRetirement;
    private BigDecimal retirementRoi;
    private BigDecimal employerContribution;
    private BigDecimal employerEarnings;
    private BigDecimal personalContribution;
    private BigDecimal personalEarnings;
    private Integer currentWorkSeniority;
    private BigDecimal predictedLumpSum;
    private BigDecimal predictedNetLumpSum;
}
