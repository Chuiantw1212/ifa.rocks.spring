package rocks.ifa.spring.domain.clientProfile.contracts;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "客戶的個人基本資料回應")
public record ProfileRes(
    UUID id,
    @Schema(description = "客戶自己的 Firebase UID (如果已綁定)")
    String clientFirebaseUid,
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
