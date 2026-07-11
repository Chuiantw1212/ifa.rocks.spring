package rocks.ifa.spring.application.agent;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;
import rocks.ifa.spring.application.agent.dto.AgentRes;
import rocks.ifa.spring.domain.agent.Agent;

@Mapper(componentModel = "spring")
public interface AgentMapper {

    AgentMapper INSTANCE = Mappers.getMapper(AgentMapper.class);

    @Mapping(source = "id", target = "uid")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "name", target = "displayName")
    @Mapping(source = "pictureUrl", target = "picture")
    @Mapping(source = "lineUserId", target = "isLineBound", qualifiedByName = "mapLineBindingStatus")
    AgentRes toAgentRes(Agent agent);

    @Named("mapLineBindingStatus")
    default boolean mapLineBindingStatus(String lineUserId) {
        return StringUtils.hasText(lineUserId);
    }
}
