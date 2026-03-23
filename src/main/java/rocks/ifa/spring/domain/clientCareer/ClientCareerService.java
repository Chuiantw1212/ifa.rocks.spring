package rocks.ifa.spring.domain.clientCareer;

public interface ClientCareerService {
    void updateCareer(String uid, ClientCareerContracts.UpdateCareerReq req);
    ClientCareerContracts.CareerRes getCareer(String uid);
}
