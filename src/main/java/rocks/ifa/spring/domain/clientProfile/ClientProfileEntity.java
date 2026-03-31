package rocks.ifa.spring.domain.clientProfile;

import jakarta.persistence.Column;
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
@Table(name = "client_profiles")
public class ClientProfileEntity extends ClientBaseEntity {

    @Column(name = "client_firebase_uid")
    private String clientFirebaseUid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    private String lineId;

    private LocalDate birthDate;
    private String gender;
    private Integer currentAge;
    private Integer retirementAge;
    private Integer lifeExpectancy;
    @Column(name = "life_expectancy_at_retirement")
    private Integer lifeExpectancyAtRetirement;
    private Integer marriageYear;
    private String careerInsuranceType;
    private String biography;
}
