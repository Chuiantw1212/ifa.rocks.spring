package rocks.ifa.spring.domain.clientCreditCards;

import rocks.ifa.spring.domain.clientCreditCards.dtos.CreateCreditCardReq;
import rocks.ifa.spring.domain.clientCreditCards.dtos.CreditCardRes;
import rocks.ifa.spring.domain.clientCreditCards.dtos.UpdateCreditCardReq;

import java.util.List;
import java.util.UUID;

public interface ClientCreditCardService {
    CreditCardRes createCreditCard(UUID clientId, CreateCreditCardReq req, String requesterUid);
    List<CreditCardRes> getCreditCardsByClientId(UUID clientId, String requesterUid);
    CreditCardRes updateCreditCard(UUID clientId, UUID cardId, UpdateCreditCardReq req, String requesterUid);
    void deleteCreditCard(UUID clientId, UUID cardId, String requesterUid);
}
