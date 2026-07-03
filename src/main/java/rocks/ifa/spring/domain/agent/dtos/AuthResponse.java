package rocks.ifa.spring.domain.agent.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing the token for client-side sign-in.")
public record AuthResponse(
    @Schema(description = "The token to be used for authentication. For Firebase, this is typically a custom token.",
            example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token
) {}
