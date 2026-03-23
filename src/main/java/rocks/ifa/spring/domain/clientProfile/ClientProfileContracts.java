package rocks.ifa.spring.domain.clientProfile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public interface ClientProfileContracts {

    record ProfileRes(
        Long id,
        LocalDate birthDate,
        String gender,
        Integer currentAge,
        Integer lifeExpectancy,
        Integer marriageYear,
        String careerInsuranceType,
        String biography
    ) {}

    record UpdateProfileReq(
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,
        
        String gender,
        Integer marriageYear,
        String biography,

        @NotBlank(message = "Career insurance type cannot be blank")
        String careerInsuranceType
    ) {}
}
