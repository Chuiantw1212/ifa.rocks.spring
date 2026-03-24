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
        return clientProfileRepository.findById(Long.parseLong(clientUid))
                .map(this::mapToFullDataRes)
                .orElse(null);
    }

    @Override
    public PageResponse<ClientFullDataRes> listClientsByAgent(String agentUid, Pageable pageable) {
        log.info("Listing clients for agent UID: {}", agentUid);

        Page<ClientProfileEntity> clientPage = clientProfileRepository.findAllByAgentFirebaseUid(agentUid, pageable);

        List<ClientFullDataRes> dtoList = clientPage.getContent().stream()
                .map(this::mapToFullDataRes)
                .collect(Collectors.toList());

        return new PageResponse<>(
                dtoList,
                clientPage.getTotalElements(),
                clientPage.getNumber(),
                clientPage.getSize()
        );
    }

    @Override
    @Transactional
    public ClientProfileContracts.ProfileRes createClient(ClientContracts.CreateClientReq req, String agentFirebaseUid) {
        log.info("Agent [{}] is creating a new client with email: {}", agentFirebaseUid, req.email());

        ClientProfileEntity newProfile = new ClientProfileEntity();
        newProfile.setAgentFirebaseUid(agentFirebaseUid);
        newProfile.setName(req.name());
        newProfile.setEmail(req.email());
        
        ClientProfileEntity savedProfile = clientProfileRepository.save(newProfile);
        log.info("✅ Successfully created new client with ID: {} for Agent UID: {}", savedProfile.getId(), agentFirebaseUid);

        return new ClientProfileContracts.ProfileRes(
            savedProfile.getId(),
            null, // birthDate is not part of creation
            null, // gender is not part of creation
            null, // currentAge
            null, // lifeExpectancy
            null, // marriageYear
            null, // careerInsuranceType
            null  // biography
        );
    }

    private ClientFullDataRes mapToFullDataRes(ClientProfileEntity entity) {
        ClientFullDataRes res = new ClientFullDataRes();
        res.setProfile(new ClientProfileContracts.ProfileRes(
                entity.getId(),
                entity.getBirthDate(),
                entity.getGender(),
                entity.getCurrentAge(),
                entity.getLifeExpectancy(),
                entity.getMarriageYear(),
                entity.getCareerInsuranceType(),
                entity.getBiography()
        ));
        res.setId(entity.getId());
        // In a real app, you would also fetch and set other parts like career, tax etc.
        // res.setCareer(clientCareerService.getCareer(entity.getAgentFirebaseUid()));
        return res;
    }
}
