package rocks.ifa.spring.domain.clientCreditCards;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.domain.clientCreditCards.dtos.CreateCreditCardReq;
import rocks.ifa.spring.domain.clientCreditCards.dtos.CreditCardRes;
import rocks.ifa.spring.domain.clientCreditCards.dtos.UpdateCreditCardReq;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientCreditCardServiceImpl implements ClientCreditCardService {

    private final ClientCreditCardRepository creditCardRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final ClientCreditCardMapper creditCardMapper;

    @Override
    @Transactional
    public CreditCardRes createCreditCard(UUID clientId, CreateCreditCardReq req, String requesterUid) {
        authorizeAccess(clientId, requesterUid);

        ClientCreditCardEntity newEntity = creditCardMapper.toEntity(req);
        newEntity.setId(UUID.randomUUID()); // Generate a new UUID for the card itself
        newEntity.setClientId(clientId); // Set the client it belongs to
        newEntity.setAgentFirebaseUid(requesterUid);
        
        ClientCreditCardEntity savedEntity = creditCardRepository.save(newEntity);
        return creditCardMapper.toRes(savedEntity);
    }

    @Override
    public List<CreditCardRes> getCreditCardsByClientId(UUID clientId, String requesterUid) {
        authorizeAccess(clientId, requesterUid);
        
        return creditCardRepository.findByClientId(clientId).stream()
                .map(creditCardMapper::toRes)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CreditCardRes updateCreditCard(UUID clientId, UUID cardId, UpdateCreditCardReq req, String requesterUid) {
        authorizeAccess(clientId, requesterUid);
        
        ClientCreditCardEntity entity = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card not found with ID: " + cardId));
        
        // Extra check to ensure the card belongs to the client specified in the URL
        if (!entity.getClientId().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit card does not belong to the specified client.");
        }
        
        creditCardMapper.updateEntityFromReq(req, entity);
        entity.setAgentFirebaseUid(requesterUid);
        
        ClientCreditCardEntity updatedEntity = creditCardRepository.save(entity);
        return creditCardMapper.toRes(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteCreditCard(UUID clientId, UUID cardId, String requesterUid) {
        authorizeAccess(clientId, requesterUid);
        
        ClientCreditCardEntity entity = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card not found with ID: " + cardId));

        // Extra check
        if (!entity.getClientId().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit card does not belong to the specified client.");
        }
        
        creditCardRepository.delete(entity);
    }

    private void authorizeAccess(UUID clientId, String requesterUid) {
        var profile = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associated client profile not found with ID: " + clientId));
        
        if (!Objects.equals(requesterUid, profile.getAgentFirebaseUid())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this client's data.");
        }
    }
}
