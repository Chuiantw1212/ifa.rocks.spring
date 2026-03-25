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
        @Schema(description = "預計退休年齡")
        Integer retirementAge,
        Integer lifeExpectancy,
        @Schema(description = "退休時預期餘命 (由後端計算)")
        Integer lifeExpectancyAtRetirement,
        Integer marriageYear,
        String careerInsuranceType,
        String biography
    ) {}

    @Schema(description = "用於更新客戶個人資料的請求 (PUT)")
    record UpdateProfileReq(
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

    @Schema(description = "用於部分更新客戶個人資料的請求 (PATCH)")
    record PatchProfileReq(
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
}
