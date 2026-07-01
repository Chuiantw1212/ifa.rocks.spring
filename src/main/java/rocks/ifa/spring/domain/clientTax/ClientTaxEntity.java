package rocks.ifa.spring.domain.clientTax;

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
@Table(name = "user_taxes")
public class ClientTaxEntity extends ClientBaseEntity {
    private BigDecimal estimatedOtherIncome;
}
