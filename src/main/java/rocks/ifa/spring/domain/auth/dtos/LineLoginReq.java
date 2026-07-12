package rocks.ifa.spring.domain.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request containing the original, unmodified ID Token from LINE.")
public record LineLoginReq(
    @NotBlank(message = "LINE ID token cannot be blank.")
    @Schema(description = "The raw ID Token string provided by the LINE SDK.", required = true)
    String idToken
) {}
