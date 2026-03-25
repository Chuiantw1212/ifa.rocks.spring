package rocks.ifa.spring.domain.clientProfile;

import org.springframework.data.domain.Pageable;
import rocks.ifa.spring.infra.common.PageResponse;

import java.util.UUID;

public interface ClientProfileService {
    void updateProfile(String uid, ClientProfileContracts.UpdateProfileReq req);
    ClientProfileContracts.ProfileRes getProfile(String uid);
    PageResponse<ClientProfileContracts.ProfileRes> listClientProfilesByAgent(String agentUid, Pageable pageable);
    ClientProfileContracts.ProfileRes getClientProfileById(UUID clientId);
}
