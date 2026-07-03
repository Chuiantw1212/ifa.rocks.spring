package rocks.ifa.spring.domain.agent.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
    @Schema(description = "The status of the authentication attempt.")
    AuthStatus status,

    @Schema(description = "The token to be used for authentication (e.g., a Firebase Custom Token). " +
                          "This is typically present only when status is SUCCESS.")
    String token,

    @Schema(description = "A message providing more context, especially for ACCOUNT_EXISTS_EMAIL_MISMATCH status.")
    String message
) {
    public enum AuthStatus {
        /** The user was successfully authenticated or created, and a token is provided. */
        SUCCESS,
        /** An account with the same email already exists, but is not linked to this login method.
            The client should prompt the user to sign in with their existing method and then link the new one. */
        ACCOUNT_EXISTS_EMAIL_MISMATCH
    }

    public static AuthResponse success(String token) {
        return new AuthResponse(AuthStatus.SUCCESS, token, null);
    }

    public static AuthResponse accountExists(String message) {
        return new AuthResponse(AuthStatus.ACCOUNT_EXISTS_EMAIL_MISMATCH, null, message);
    }
}
