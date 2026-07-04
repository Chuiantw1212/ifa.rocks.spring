package rocks.ifa.spring.domain.agent.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
    @Schema(description = "The status of the authentication attempt.")
    AuthStatus status,

    @Schema(description = "The Firebase Custom Token to be used for client-side sign-in. " +
                          "This is typically present only when status is SUCCESS.")
    String customToken,

    @Schema(description = "A message providing more context, especially for failure or redirect statuses.")
    String message
) {
    public enum AuthStatus {
        SUCCESS,
        USER_NOT_FOUND,
        REDIRECT_TO_FIREBASE_LOGIN
    }

    public static AuthResponse success(String customToken) {
        return new AuthResponse(AuthStatus.SUCCESS, customToken, null);
    }

    public static AuthResponse userNotFound(String message) {
        return new AuthResponse(AuthStatus.USER_NOT_FOUND, null, message);
    }

    public static AuthResponse redirectToFirebase(String message) {
        return new AuthResponse(AuthStatus.REDIRECT_TO_FIREBASE_LOGIN, null, message);
    }
}
