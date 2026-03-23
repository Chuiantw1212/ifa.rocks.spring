package rocks.ifa.spring.domain.agent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

/**
 * A container interface for all Data Transfer Objects (DTOs) related to the Agent domain.
 * Using records for concise, immutable data carriers.
 */
public interface AgentContracts {

    record LoginReq(
        @NotBlank(message = "Firebase token cannot be blank")
        String firebaseToken
    ) {}

    record CreateAgentReq(
        @NotBlank @Email
        String email,
        @NotBlank
        String password,
        String displayName
    ) {}

    record UpdateAgentReq(
        String displayName,
        boolean disabled
    ) {}

    record AgentRes(
        String uid,
        String email,
        String displayName,
        boolean disabled,
        OffsetDateTime creationTimestamp,
        OffsetDateTime lastSignInTimestamp
    ) {}

    record AuthRes(
        String token,
        AgentRes agent
    ) {}
}
