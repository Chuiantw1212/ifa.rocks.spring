package rocks.ifa.spring.domain.agent.contracts;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(
    @NotBlank(message = "Firebase token cannot be blank")
    String firebaseToken
) {}
