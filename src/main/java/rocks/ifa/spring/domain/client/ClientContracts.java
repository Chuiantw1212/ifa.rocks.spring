package rocks.ifa.spring.domain.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface ClientContracts {

    @Schema(description = "用於建立新客戶的請求")
    record CreateClientReq(
        @Schema(description = "客戶姓名", example = "王大明")
        @NotBlank(message = "客戶姓名不能為空")
        String name,

        @Schema(description = "客戶 Email", example = "david.wang@example.com")
        @NotBlank(message = "Email 不能為空")
        @Email(message = "Email 格式不正確")
        String email
    ) {}
}
