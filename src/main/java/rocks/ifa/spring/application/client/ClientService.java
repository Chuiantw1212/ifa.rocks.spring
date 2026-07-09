package rocks.ifa.spring.application.client;

import org.springframework.data.domain.Pageable;
import rocks.ifa.spring.application.client.dtos.ClientFullDataRes;
import rocks.ifa.spring.application.client.dtos.CreateClientReq;
import rocks.ifa.spring.application.clientProfile.dtos.ProfileRes;
import rocks.ifa.spring.infrastructure.common.PageResponse;

import java.util.UUID;

public interface ClientService {

    ClientFullDataRes getClientFullData(UUID clientId, String requesterUid);
    
    PageResponse<ClientFullDataRes> listClientsByAgent(String agentUid, Pageable pageable);

    ProfileRes createClient(CreateClientReq req, String agentFirebaseUid);

    void deleteClient(UUID clientId, String requesterUid);
}
