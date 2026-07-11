package rocks.ifa.spring.application.agent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAgentReq(
    @NotBlank @Email
    String email,
    @NotBlank
    String password,
    String displayName
) {}
