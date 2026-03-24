package rocks.ifa.spring.domain.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.clientProfile.ClientProfileContracts;
import rocks.ifa.spring.domain.clientProfile.ClientProfileEntity;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.infra.SecurityUtils;
import rocks.ifa.spring.infra.common.PageResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientProfileRepository clientProfileRepository;

    @Override
    public ClientFullDataRes getClientFullData(String clientUid) {
        // ...
        return null;
    }

    @Override
    public PageResponse<ClientFullDataRes> listClientsByAgent(String agentUid, Pageable pageable) {
        // ...
        return null;
    }

    @Override
    @Transactional
    public ClientProfileContracts.ProfileRes createClient(ClientContracts.CreateClientReq req) {
        String agentFirebaseUid = SecurityUtils.getCurrentUserUid();
        log.info("Agent [{}] is creating a new client with email: {}", agentFirebaseUid, req.email());

        // You might want to check if this agent already has a client with the same email.
        // if (clientProfileRepository.existsByAgentFirebaseUidAndEmail(agentFirebaseUid, req.email())) {
        //     throw new IllegalStateException("This agent already has a client with the same email.");
        // }

        ClientProfileEntity newProfile = new ClientProfileEntity();
        newProfile.setAgentFirebaseUid(agentFirebaseUid);
        newProfile.setName(req.name());
        newProfile.setEmail(req.email());
        
        ClientProfileEntity savedProfile = clientProfileRepository.save(newProfile);
        log.info("✅ Successfully created new client with ID: {} for Agent UID: {}", savedProfile.getId(), agentFirebaseUid);

        return new ClientProfileContracts.ProfileRes(
            savedProfile.getId(),
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
        // ...
        return null;
    }
}
