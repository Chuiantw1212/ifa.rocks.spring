package rocks.ifa.spring.application.clientCareer;

import rocks.ifa.spring.application.clientCareer.dtos.CareerRes;
import rocks.ifa.spring.application.clientCareer.dtos.UpdateCareerReq;

import java.util.Optional;
import java.util.UUID;

public interface ClientCareerService {
    void updateCareer(UUID clientId, UpdateCareerReq req, String requesterUid);
    Optional<CareerRes> getCareer(UUID clientId, String requesterUid);
}
