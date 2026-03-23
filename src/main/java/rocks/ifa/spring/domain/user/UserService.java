package rocks.ifa.spring.domain.user;

public interface UserService {
    UserFullDataRes getFullUserData(String uid);
    void deleteUser(String uid);
    void registerUser(UserRegistrationReq req);
}
