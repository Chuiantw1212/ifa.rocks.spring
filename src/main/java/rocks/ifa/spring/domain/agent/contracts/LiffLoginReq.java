package rocks.ifa.spring.domain.agent.contracts;

import jakarta.validation.constraints.NotBlank;

public record LiffLoginReq(
    @NotBlank(message = "LIFF ID Token cannot be blank")
    String token
) {}
