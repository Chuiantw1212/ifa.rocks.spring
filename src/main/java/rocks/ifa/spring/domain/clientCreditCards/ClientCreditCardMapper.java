package rocks.ifa.spring.domain.clientCreditCards;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rocks.ifa.spring.domain.clientCreditCards.dtos.CreateCreditCardReq;
import rocks.ifa.spring.domain.clientCreditCards.dtos.CreditCardRes;
import rocks.ifa.spring.domain.clientCreditCards.dtos.UpdateCreditCardReq;

@Mapper(componentModel = "spring")
public interface ClientCreditCardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "agentFirebaseUid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ClientCreditCardEntity toEntity(CreateCreditCardReq req);

    CreditCardRes toRes(ClientCreditCardEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "agentFirebaseUid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromReq(UpdateCreditCardReq req, @MappingTarget ClientCreditCardEntity entity);
}
