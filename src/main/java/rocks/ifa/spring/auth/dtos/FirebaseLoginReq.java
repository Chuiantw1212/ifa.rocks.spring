package rocks.ifa.spring.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request containing the Firebase ID Token obtained from the client-side SDK after a user signs in or signs up.")
public record FirebaseLoginReq(
    @NotBlank(message = "Firebase ID token cannot be blank.")
    @Schema(description = "The ID Token provided by the Firebase Client SDK.", required = true)
    String idToken
) {}
