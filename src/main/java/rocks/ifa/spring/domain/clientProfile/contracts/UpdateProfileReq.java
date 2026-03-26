package rocks.ifa.spring.domain.clientProfile.contracts;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

@Schema(description = "用於更新客戶個人資料的請求 (PUT)")
public record UpdateProfileReq(
    @NotBlank String name,
    @NotBlank String email,
    String phone,
    String lineId,
    @Past LocalDate birthDate,
    String gender,
    Integer retirementAge,
    Integer marriageYear,
    String careerInsuranceType,
    String biography
) {}
