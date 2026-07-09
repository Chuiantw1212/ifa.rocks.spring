package rocks.ifa.spring.application.agent.dto;

public record UpdateAgentReq(
    String displayName,
    boolean disabled
) {}
