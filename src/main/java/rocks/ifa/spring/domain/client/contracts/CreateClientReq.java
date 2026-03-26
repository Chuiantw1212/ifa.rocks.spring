package rocks.ifa.spring.domain.client.contracts;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "用於建立新客戶的請求")
public record CreateClientReq(
    @Schema(description = "客戶姓名", example = "王大明")
    @NotBlank(message = "客戶姓名不能為空")
    String name,

    @Schema(description = "客戶 Email", example = "david.wang@example.com")
    @NotBlank(message = "Email 不能為空")
    @Email(message = "Email 格式不正確")
    String email,

    @Schema(description = "客戶電話", example = "0912345678")
    String phone,

    @Schema(description = "客戶 Line ID", example = "david_wang")
    String lineId
) {}
