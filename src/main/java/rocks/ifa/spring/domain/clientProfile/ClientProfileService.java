package rocks.ifa.spring.domain.clientProfile;

public interface ClientProfileService {
    void updateProfile(String uid, ClientProfileContracts.UpdateProfileReq req);
    ClientProfileContracts.ProfileRes getProfile(String uid);
}
