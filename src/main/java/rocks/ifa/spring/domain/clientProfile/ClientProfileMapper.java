package rocks.ifa.spring.domain.clientProfile;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rocks.ifa.spring.domain.clientProfile.dtos.ProfileRes;

@Mapper(componentModel = "spring")
public interface ClientProfileMapper {

    ClientProfileMapper INSTANCE = Mappers.getMapper(ClientProfileMapper.class);

    ProfileRes toProfileRes(ClientProfileEntity entity);
}
