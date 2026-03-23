package rocks.ifa.spring.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserProfileUpdateReq {

    private LocalDate birthDate;
    private String gender;
    private Integer marriageYear; // ✅ Reverted to Integer
    private String biography;
    private String careerInsuranceType;
}
