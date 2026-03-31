package rocks.ifa.spring.domain.client;

import org.springframework.data.domain.Pageable;
import rocks.ifa.spring.domain.client.contracts.ClientFullDataRes;
import rocks.ifa.spring.domain.client.contracts.CreateClientReq;
import rocks.ifa.spring.domain.clientProfile.contracts.ProfileRes;
import rocks.ifa.spring.infra.common.PageResponse;

import java.util.UUID;

public interface ClientService {

    ClientFullDataRes getClientFullData(String clientUid);
    
    PageResponse<ClientFullDataRes> listClientsByAgent(String agentUid, Pageable pageable);

    ProfileRes createClient(CreateClientReq req, String agentFirebaseUid);

    void deleteClient(UUID clientId, String requesterUid);
}
