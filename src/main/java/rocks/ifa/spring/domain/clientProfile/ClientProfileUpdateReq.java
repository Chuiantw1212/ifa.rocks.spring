package rocks.ifa.spring.domain.clientProfile;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ClientProfileUpdateReq {
    private LocalDate birthDate;
    private String gender;
    private Integer marriageYear;
    private String biography;
    private String careerInsuranceType;
}
