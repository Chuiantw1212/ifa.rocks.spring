package rocks.ifa.spring.domain.clientProfile.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

@Schema(description = "用於部分更新客戶個人資料的請求 (PATCH)")
public record PatchProfileReq(
    String name,
    String email,
    String phone,
    String lineId,
    @Past LocalDate birthDate,
    String gender,
    Integer retirementAge,
    Integer marriageYear,
    String careerInsuranceType,
    String biography
) {}
