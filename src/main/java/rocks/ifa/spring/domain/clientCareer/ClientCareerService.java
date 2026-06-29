package rocks.ifa.spring.domain.clientCareer;

import rocks.ifa.spring.domain.clientCareer.dtos.CareerRes;
import rocks.ifa.spring.domain.clientCareer.dtos.UpdateCareerReq;

import java.util.UUID;

public interface ClientCareerService {
    void updateCareer(UUID clientId, UpdateCareerReq req, String requesterUid);
    CareerRes getCareer(UUID clientId, String requesterUid);
}
