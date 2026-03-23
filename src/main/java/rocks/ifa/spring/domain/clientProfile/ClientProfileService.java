package rocks.ifa.spring.domain.clientProfile;

public interface ClientProfileService {
    void updateProfile(String uid, UserProfileUpdateReq req);
    UserProfileDto getProfile(String uid);
}
