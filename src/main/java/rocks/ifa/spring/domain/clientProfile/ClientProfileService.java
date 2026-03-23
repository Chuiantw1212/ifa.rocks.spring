package rocks.ifa.spring.domain.clientProfile;

public interface ClientProfileService {
    void updateProfile(String uid, ClientProfileUpdateReq req);
    ClientProfileDto getProfile(String uid);
}
