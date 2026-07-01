package rocks.ifa.spring.domain.agent.dtos;

import jakarta.validation.constraints.NotBlank;

public record LiffLoginReq(
    @NotBlank(message = "LIFF ID Token cannot be blank")
    String token
) {}
