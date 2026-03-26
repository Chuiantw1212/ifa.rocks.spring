package rocks.ifa.spring.domain.clientCareer;

import rocks.ifa.spring.domain.clientCareer.contracts.CareerRes;
import rocks.ifa.spring.domain.clientCareer.contracts.UpdateCareerReq;

public interface ClientCareerService {
    void updateCareer(String uid, UpdateCareerReq req);
    CareerRes getCareer(String uid);
}
