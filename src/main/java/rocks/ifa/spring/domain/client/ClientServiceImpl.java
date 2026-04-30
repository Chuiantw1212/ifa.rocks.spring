package rocks.ifa.spring.domain.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.domain.client.contracts.ClientFullDataRes;
import rocks.ifa.spring.domain.client.contracts.CreateClientReq;
import rocks.ifa.spring.domain.clientCareer.ClientCareerService;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceService;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionService;
import rocks.ifa.spring.domain.clientProfile.ClientProfileEntity;
import rocks.ifa.spring.domain.clientProfile.ClientProfileMapper;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.domain.clientProfile.ClientProfileService;
import rocks.ifa.spring.domain.clientProfile.contracts.ProfileRes;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementService;
import rocks.ifa.spring.domain.clientTax.ClientTaxService;
import rocks.ifa.spring.infra.common.PageResponse;

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
    private final ClientProfileMapper clientProfileMapper;

    @Override
    public ClientFullDataRes getClientFullData(UUID clientId, String requesterUid) {
        return clientProfileRepository.findById(clientId)
                .map(entity -> mapToFullDataRes(entity, requesterUid))
                .orElse(null);
    }

    @Override
    public PageResponse<ClientFullDataRes> listClientsByAgent(String agentUid, Pageable pageable) {
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
        return new ClientFullDataRes(
                clientId,
                clientProfileService.getClientProfileById(clientId, requesterUid),
                clientCareerService.getCareer(clientId, requesterUid),
                clientLaborPensionService.getLaborPension(clientId.toString()),
                clientLaborInsuranceService.getLaborInsurance(clientId.toString()),
                clientRetirementService.getRetirement(clientId.toString()),
                clientTaxService.getTax(clientId.toString())
        );
    }
}
