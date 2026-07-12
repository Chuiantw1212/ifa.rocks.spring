package rocks.ifa.spring.domain.agent;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;
import rocks.ifa.spring.domain.agent.dtos.AgentRecord;

@Mapper(componentModel = "spring")
public interface AgentMapper {

    @Mapping(source = "id", target = "uid")
    @Mapping(source = "name", target = "displayName")
    @Mapping(source = "pictureUrl", target = "picture")
    @Mapping(source = "lineUserId", target = "isLineBound", qualifiedByName = "mapLineBindingStatus")
    AgentRecord toRecord(Agent agent);

    @Named("mapLineBindingStatus")
    default boolean mapLineBindingStatus(String lineUserId) {
        return StringUtils.hasText(lineUserId);
    }
}
