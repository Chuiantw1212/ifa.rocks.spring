package rocks.ifa.spring.domain.clientProfile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.UUID;

public interface ClientProfileContracts {

    @Schema(description = "客戶的個人基本資料回應")
    record ProfileRes(
            UUID id,
            String name,
            String email,
            String phone,
            String lineId,
            LocalDate birthDate,
            String gender,
            Integer currentAge,
            Integer lifeExpectancy,
            @Schema(description = "退休時預期餘命 (由後端計算)")
            Integer lifeExpectancyAtRetirement,
            Integer marriageYear,
            String careerInsuranceType,
            String biography
    ) {}

    // ... other records remain the same
}
