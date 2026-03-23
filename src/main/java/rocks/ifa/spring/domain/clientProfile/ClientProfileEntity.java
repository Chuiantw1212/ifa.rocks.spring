package rocks.ifa.spring.domain.clientProfile;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import rocks.ifa.spring.domain.common.ClientBaseEntity;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_profiles")
public class ClientProfileEntity extends ClientBaseEntity {

    private LocalDate birthDate;
    private String gender;
    private Integer currentAge;
    private Integer lifeExpectancy;
    private Integer marriageYear;
    private String careerInsuranceType;
    private String biography;
}
