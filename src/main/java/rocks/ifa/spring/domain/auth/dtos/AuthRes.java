package rocks.ifa.spring.domain.auth.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import rocks.ifa.spring.domain.agent.dtos.AgentRes;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthRes(String customToken, AgentRes agent) {
}
