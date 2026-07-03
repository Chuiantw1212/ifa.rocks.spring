package rocks.ifa.spring.domain.line;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents the payload of a LINE ID Token (OpenID Connect).
 * This record is used to capture the essential user information from the token
 * after it has been verified.
 *
 * @param iss The issuer of the token (e.g., "https://access.line.me").
 * @param sub The unique identifier for the LINE user. This is the primary key for LINE users.
 * @param aud The audience for which the token was issued (your LINE Login channel ID).
 * @param exp The expiration time of the token (in seconds since epoch).
 * @param iat The time at which the token was issued (in seconds since epoch).
 * @param name The user's display name.
 * @param picture The URL of the user's profile image.
 */
@Schema(description = "LINE ID Token 的 payload，包含使用者基本資訊")
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

    @Schema(description = "LINE 使用者的顯示名稱")
    String name,

    @Schema(description = "LINE 使用者頭像的 URL")
    String picture
) {}
