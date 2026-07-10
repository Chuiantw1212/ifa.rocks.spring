package rocks.ifa.spring.application.clientProfile;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rocks.ifa.spring.application.clientProfile.dtos.ProfileRes;
import rocks.ifa.spring.domain.clientProfile.ClientProfileEntity;

@Mapper(componentModel = "spring")
public interface ClientProfileMapper {

    ClientProfileMapper INSTANCE = Mappers.getMapper(ClientProfileMapper.class);

    ProfileRes toProfileRes(ClientProfileEntity entity);
}
