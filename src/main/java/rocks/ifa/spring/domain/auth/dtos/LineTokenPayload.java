package rocks.ifa.spring.domain.auth.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents the payload of a LINE ID Token (OpenID Connect).
 * This record is used to capture the essential user information from the token
 * after it has been verified.
 */
@Schema(description = "LINE ID Token 的 payload，包含使用者基本資訊")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LineTokenPayload(
    @Schema(description = "Token 發行者", example = "https://access.line.me")
    String iss,

    @NotBlank(message = "LINE User ID (sub) cannot be blank")
    @Schema(description = "LINE 使用者的唯一識別碼", required = true)
    String sub,

    @Schema(description = "此 Token 的受眾，即你的 LINE Login Channel ID")
    String aud,

    @Schema(description = "Token 的過期時間 (Unix timestamp)")
    Long exp,

    @Schema(description = "Token 的發行時間 (Unix timestamp)")
    Long iat,

    @Schema(description = "使用者的電子郵件。只有在申請並取得權限後才會出現。", nullable = true)
    String email,

    @Schema(description = "LINE 使用者的顯示名稱")
    String name,

    @Schema(description = "LINE 使用者頭像的 URL")
    String picture
) {}
