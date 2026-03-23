package rocks.ifa.spring.domain.clientProfile;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ClientProfileDto {
    private Long id;
    private LocalDate birthDate;
    private String gender;
    private Integer currentAge;
    private Integer lifeExpectancy;
    private Integer marriageYear;
    private String careerInsuranceType;
    private String biography;
}
