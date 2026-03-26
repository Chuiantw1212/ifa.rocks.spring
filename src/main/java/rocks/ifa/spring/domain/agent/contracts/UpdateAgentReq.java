package rocks.ifa.spring.domain.agent.contracts;

public record UpdateAgentReq(
    String displayName,
    boolean disabled
) {}
