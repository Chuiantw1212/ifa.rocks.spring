package rocks.ifa.spring.domain.client;

import org.springframework.data.domain.Pageable;
import rocks.ifa.spring.domain.clientProfile.ClientProfileContracts;
import rocks.ifa.spring.infra.common.PageResponse;

public interface ClientService {

    ClientFullDataRes getClientFullData(String clientUid);
    
    PageResponse<ClientFullDataRes> listClientsByAgent(String agentUid, Pageable pageable);

    ClientProfileContracts.ProfileRes createClient(ClientContracts.CreateClientReq req, String agentFirebaseUid);
}
