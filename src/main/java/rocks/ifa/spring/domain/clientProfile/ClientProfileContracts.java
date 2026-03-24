package rocks.ifa.spring.domain.clientProfile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.UUID;

public interface ClientProfileContracts {

    @Schema(description = "客戶的個人基本資料回應")
    record ProfileRes(
            @Schema(description = "系統內部 ID (UUID)")
            UUID id,
            @Schema(description = "姓名")
            String name,
            @Schema(description = "Email")
            String email,
            @Schema(description = "電話")
            String phone,
            @Schema(description = "Line ID")
            String lineId,
            @Schema(description = "出生日期 (ISO 8601 格式)", example = "1990-12-12")
            LocalDate birthDate,
            @Schema(description = "性別", example = "FEMALE")
            String gender,
            @Schema(description = "當前年齡 (由後端計算)", example = "33")
            Integer currentAge,
            @Schema(description = "預期壽命 (由後端計算)", example = "85")
            Integer lifeExpectancy,
            @Schema(description = "結婚年份", example = "2020")
            Integer marriageYear,
            @Schema(description = "職業保險類別", example = "LABOR")
            String careerInsuranceType,
            @Schema(description = "個人簡介或理財故事")
            String biography
    ) {
    }

    @Schema(description = "用於更新客戶個人資料的請求")
    record UpdateProfileReq(
            @Schema(description = "出生日期 (ISO 8601 格式)", example = "1990-12-12")
            @Past(message = "Birth date must be in the past")
            LocalDate birthDate,

            @Schema(description = "性別", example = "FEMALE")
            String gender,

            @Schema(description = "結婚年份", example = "2020")
            Integer marriageYear,

            @Schema(description = "個人簡介或理財故事")
            String biography,

            @Schema(description = "職業保險類別", example = "LABOR")
            @NotBlank(message = "Career insurance type cannot be blank")
            String careerInsuranceType
    ) {
    }
}
