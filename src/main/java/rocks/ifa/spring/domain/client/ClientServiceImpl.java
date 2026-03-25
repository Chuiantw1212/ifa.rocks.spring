package rocks.ifa.spring.domain.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.clientCareer.ClientCareerService;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceService;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionService;
import rocks.ifa.spring.domain.clientProfile.ClientProfileContracts;
import rocks.ifa.spring.domain.clientProfile.ClientProfileEntity;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.domain.clientProfile.ClientProfileService;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementService;
import rocks.ifa.spring.domain.clientTax.ClientTaxService;
import rocks.ifa.spring.infra.common.PageResponse;

import java.util.List;
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

    @Override
    public ClientFullDataRes getClientFullData(String clientUid) {
        return clientProfileRepository.findById(UUID.fromString(clientUid))
                .map(this::mapToFullDataRes)
                .orElse(null);
    }

    @Override
    public PageResponse<ClientFullDataRes> listClientsByAgent(String agentUid, Pageable pageable) {
        Page<ClientProfileEntity> clientPage = clientProfileRepository.findAllByAgentFirebaseUid(agentUid, pageable);
        List<ClientFullDataRes> dtoList = clientPage.getContent().stream()
                .map(this::mapToFullDataRes)
                .collect(Collectors.toList());
        return new PageResponse<>(dtoList, clientPage.getTotalElements(), clientPage.getNumber(), clientPage.getSize());
    }

    @Override
    @Transactional
    public ClientProfileContracts.ProfileRes createClient(ClientContracts.CreateClientReq req, String agentFirebaseUid) {
        log.info("Agent [{}] is creating a new client with email: {}", agentFirebaseUid, req.email());

        ClientProfileEntity newProfile = new ClientProfileEntity();
        newProfile.setId(UUID.randomUUID());
        newProfile.setAgentFirebaseUid(agentFirebaseUid);
        newProfile.setName(req.name());
        newProfile.setEmail(req.email());
        newProfile.setPhone(req.phone());
        newProfile.setLineId(req.lineId());

        ClientProfileEntity savedProfile = clientProfileRepository.save(newProfile);
        log.info("✅ Successfully created new client with ID: {} for Agent UID: {}", savedProfile.getId(), agentFirebaseUid);

        return new ClientProfileContracts.ProfileRes(
                savedProfile.getId(),
                savedProfile.getName(),
                savedProfile.getEmail(),
                savedProfile.getPhone(),
                savedProfile.getLineId(),
                savedProfile.getBirthDate(),
                savedProfile.getGender(),
                savedProfile.getCurrentAge(),
                savedProfile.getLifeExpectancy(),
                savedProfile.getMarriageYear(),
                savedProfile.getCareerInsuranceType(),
                savedProfile.getBiography()
        );
    }

    private ClientFullDataRes mapToFullDataRes(ClientProfileEntity entity) {
        ClientFullDataRes res = new ClientFullDataRes();
        
        // Correctly map all 12 fields to the ProfileRes record
        res.setProfile(new ClientProfileContracts.ProfileRes(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getLineId(),
                entity.getBirthDate(),
                entity.getGender(),
                entity.getCurrentAge(),
                entity.getLifeExpectancy(),
                entity.getMarriageYear(),
                entity.getCareerInsuranceType(),
                entity.getBiography()
        ));
        
        res.setId(entity.getId());
        // Here you would call other services to populate the rest of the ClientFullDataRes
        // res.setCareer(clientCareerService.getCareer(...));
        
        return res;
    }
}
