package rocks.ifa.spring.domain.agent;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import rocks.ifa.spring.domain.agent.dtos.AgentRes;

@Mapper(componentModel = "spring")
public interface AgentMapper {

    AgentMapper INSTANCE = Mappers.getMapper(AgentMapper.class);

    @Mapping(source = "id", target = "uid") // Corrected: target is now "uid"
    @Mapping(source = "name", target = "name")
    @Mapping(source = "pictureUrl", target = "picture")
    AgentRes toAgentRes(AgentEntity agent);
}
