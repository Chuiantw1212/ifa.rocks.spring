package rocks.ifa.spring.domain.client;

import org.springframework.data.domain.Pageable;
import rocks.ifa.spring.domain.client.dtos.ClientFullDataRes;
import rocks.ifa.spring.domain.client.dtos.CreateClientReq;
import rocks.ifa.spring.domain.clientProfile.dtos.ProfileRes;
import rocks.ifa.spring.infra.common.PageResponse;

import java.util.UUID;

public interface ClientService {

    ClientFullDataRes getClientFullData(UUID clientId, String requesterUid);
    
    PageResponse<ClientFullDataRes> listClientsByAgent(String agentUid, Pageable pageable);

    ProfileRes createClient(CreateClientReq req, String agentFirebaseUid);

    void deleteClient(UUID clientId, String requesterUid);
}
