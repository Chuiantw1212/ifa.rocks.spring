package rocks.ifa.spring.application.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import rocks.ifa.spring.domain.agent.dtos.AgentRes;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthRes(String customToken, AgentRes agent) {
}
