package rocks.ifa.spring.application.clientProfile;

import org.springframework.data.domain.Pageable;
import rocks.ifa.client.dto.PageResponse;
import rocks.ifa.spring.application.clientProfile.dtos.PatchProfileReq;
import rocks.ifa.spring.application.clientProfile.dtos.ProfileRes;
import rocks.ifa.spring.application.clientProfile.dtos.UpdateProfileReq;

import java.util.UUID;

public interface ClientProfileService {
    ProfileRes getOwnProfile(String clientFirebaseUid);
    ProfileRes updateProfile(UUID clientId, UpdateProfileReq req, String requesterUid);
    ProfileRes patchProfile(UUID clientId, PatchProfileReq req, String requesterUid);
    ProfileRes getProfile(String uid);
    PageResponse<ProfileRes> listClientProfilesByAgent(String agentUid, Pageable pageable);
    ProfileRes getClientProfileById(UUID clientId, String requesterUid);
}
