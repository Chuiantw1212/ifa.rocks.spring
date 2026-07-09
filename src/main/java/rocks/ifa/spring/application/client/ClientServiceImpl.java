package rocks.ifa.spring.application.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.application.client.dtos.ClientFullDataRes;
import rocks.ifa.spring.application.client.dtos.CreateClientReq;
import rocks.ifa.spring.application.clientCareer.ClientCareerService;
import rocks.ifa.spring.application.clientLaborInsurance.ClientLaborInsuranceService;
import rocks.ifa.spring.application.clientLaborPension.ClientLaborPensionService;
import rocks.ifa.spring.application.clientProfile.ClientProfileServiceImpl;
import rocks.ifa.spring.domain.clientProfile.ClientProfileEntity;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.application.clientProfile.ClientProfileService;
import rocks.ifa.spring.application.clientProfile.dtos.ProfileRes;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementService;
import rocks.ifa.spring.domain.clientTax.ClientTaxService;
import rocks.ifa.spring.infrastructure.common.PageResponse;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientProfileService clientProfileService;
    private final ClientCareerService clientCareerService;
    private final ClientLaborPensionService clientLaborPensionService;
    private final ClientLaborInsuranceService clientLaborInsuranceService;
    private final ClientRetirementService clientRetirementService;
    private final ClientTaxService clientTaxService;
    private final ClientProfileRepository clientProfileRepository;
    private final ClientProfileServiceImpl.ClientProfileMapper clientProfileMapper;

    @Override
    public ClientFullDataRes getClientFullData(UUID clientId, String requesterUid) {
        log.info("Fetching full data for client ID: {}", clientId);
        return clientProfileRepository.findById(clientId)
                .map(entity -> mapToFullDataRes(entity, requesterUid))
                .orElse(null);
    }

    @Override
    public PageResponse<ClientFullDataRes> listClientsByAgent(String agentUid, Pageable pageable) {
        log.info("Listing clients for agent UID: {}", agentUid);
        Page<ClientProfileEntity> clientPage = clientProfileRepository.findAllByAgentFirebaseUid(agentUid, pageable);
        List<ClientFullDataRes> dtoList = clientPage.getContent().stream()
                .map(entity -> mapToFullDataRes(entity, agentUid))
                .collect(Collectors.toList());
        return new PageResponse<>(dtoList, clientPage.getTotalElements(), clientPage.getNumber(), clientPage.getSize());
    }

    @Override
    @Transactional
    public ProfileRes createClient(CreateClientReq req, String agentFirebaseUid) {
        log.info("Agent [{}] is creating a new client with email: {}", agentFirebaseUid, req.email());

        ClientProfileEntity newProfile = new ClientProfileEntity();
        newProfile.setId(UUID.randomUUID());
        newProfile.setAgentFirebaseUid(agentFirebaseUid);
        newProfile.setName(req.name());
        newProfile.setEmail(req.email());
        newProfile.setPhone(req.phone());
        newProfile.setLineId(req.lineId());
        newProfile.setRetirementAge(65);

        ClientProfileEntity savedProfile = clientProfileRepository.save(newProfile);
        log.info("✅ Successfully created new client with ID: {} for Agent UID: {}", savedProfile.getId(), agentFirebaseUid);

        return clientProfileMapper.toProfileRes(savedProfile);
    }

    @Override
    @Transactional
    public void deleteClient(UUID clientId, String requesterUid) {
        log.info("Attempting to delete client ID: {}", clientId);
        ClientProfileEntity clientProfile = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        boolean isOwnerAgent = Objects.equals(requesterUid, clientProfile.getAgentFirebaseUid());
        boolean isClientSelf = Objects.equals(requesterUid, clientProfile.getClientFirebaseUid());

        if (!isOwnerAgent && !isClientSelf) {
            log.warn("Unauthorized attempt to delete client {}. Requester UID: {}", clientId, requesterUid);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this client.");
        }

        clientProfileRepository.deleteById(clientId);
        log.info("✅ Successfully deleted client with ID: {} by requester: {}", clientId, requesterUid);
    }

    private ClientFullDataRes mapToFullDataRes(ClientProfileEntity entity, String requesterUid) {
        UUID clientId = entity.getId();
        log.debug("Mapping full data response for client ID: {}", clientId);

        try {
            log.debug("Fetching profile...");
            var profile = clientProfileService.getClientProfileById(clientId, requesterUid);
            
            log.debug("Fetching career...");
            var career = clientCareerService.getCareer(clientId, requesterUid).orElse(null);
            
            log.debug("Fetching labor pension...");
            var laborPension = clientLaborPensionService.getLaborPension(clientId, requesterUid).orElse(null);
            
            log.debug("Fetching labor insurance...");
            var laborInsurance = clientLaborInsuranceService.getLaborInsurance(clientId, requesterUid).orElse(null);
            
            log.debug("Fetching retirement...");
            // Assuming these will also be updated to return Optional eventually
            var retirement = clientRetirementService.getRetirement(clientId.toString());
            
            log.debug("Fetching tax...");
            var tax = clientTaxService.getTax(clientId.toString());

            log.debug("All sub-domain data fetched successfully for client ID: {}", clientId);
            
            return new ClientFullDataRes(
                    clientId,
                    profile,
                    career,
                    laborPension,
                    laborInsurance,
                    retirement,
                    tax
            );
        } catch (Exception e) {
            log.error("❌ Error occurred during mapToFullDataRes for client ID: {}. This will be caught by GlobalExceptionHandler.", clientId, e);
            throw e; // Re-throw to be caught by the GlobalExceptionHandler
        }
    }
}
