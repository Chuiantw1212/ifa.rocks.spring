package rocks.ifa.spring.domain.agent.dtos;

import java.util.UUID;

public record AgentRecord(
    UUID uid,
    String email,
    String displayName,
    String picture,
    boolean isLineBound
) {}
