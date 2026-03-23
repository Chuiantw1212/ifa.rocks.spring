package rocks.ifa.spring.domain.clientCareer;

public interface ClientCareerService {
    void updateCareer(String uid, ClientCareerUpdateReq req);
    ClientCareerRes getCareer(String uid);
}
