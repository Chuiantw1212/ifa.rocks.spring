package rocks.ifa.spring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_profiles")
public class UserProfile extends UserBaseEntity {

    private LocalDate birthDate;
    private String gender;
    private Integer currentAge;
    private Integer lifeExpectancy;
    private Integer marriageYear;
    private String careerInsuranceType;
    private String biography;
}