package rocks.ifa.spring.domain.clientCareer;

public interface ClientCareerService {
    void updateCareer(String uid, UserCareerUpdateReq req);
    UserCareerRes getCareer(String uid);
}
