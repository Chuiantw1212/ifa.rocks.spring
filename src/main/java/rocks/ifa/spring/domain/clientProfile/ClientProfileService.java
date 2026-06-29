package rocks.ifa.spring.domain.clientProfile;

import org.springframework.data.domain.Pageable;
import rocks.ifa.spring.domain.clientProfile.dtos.PatchProfileReq;
import rocks.ifa.spring.domain.clientProfile.dtos.ProfileRes;
import rocks.ifa.spring.domain.clientProfile.dtos.UpdateProfileReq;
import rocks.ifa.spring.infra.common.PageResponse;

import java.util.UUID;

public interface ClientProfileService {
    ProfileRes getOwnProfile(String clientFirebaseUid);
    ProfileRes updateProfile(UUID clientId, UpdateProfileReq req, String requesterUid);
    ProfileRes patchProfile(UUID clientId, PatchProfileReq req, String requesterUid);
    ProfileRes getProfile(String uid);
    PageResponse<ProfileRes> listClientProfilesByAgent(String agentUid, Pageable pageable);
    ProfileRes getClientProfileById(UUID clientId, String requesterUid);
}
