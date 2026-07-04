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

    @Schema(description = "A message providing more context, especially for failure or redirect statuses.")
    String message
) {
    public enum AuthStatus {
        /** The user was successfully authenticated, and a token is provided. */
        SUCCESS,
        /** The user was not found in the system. The client should guide them to a registration flow. */
        USER_NOT_FOUND,
        /** An account with the same email already exists. The client should redirect the user to the standard Firebase login flow. */
        REDIRECT_TO_FIREBASE_LOGIN
    }

    public static AuthResponse success(String token) {
        return new AuthResponse(AuthStatus.SUCCESS, token, null);
    }

    public static AuthResponse userNotFound(String message) {
        return new AuthResponse(AuthStatus.USER_NOT_FOUND, null, message);
    }

    public static AuthResponse redirectToFirebase(String message) {
        return new AuthResponse(AuthStatus.REDIRECT_TO_FIREBASE_LOGIN, null, message);
    }
}
