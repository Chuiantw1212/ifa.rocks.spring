package rocks.ifa.spring.service;

import rocks.ifa.spring.dto.UserCareerDto;
import rocks.ifa.spring.dto.UserCareerUpdateReq;

public interface UserCareerService {
    UserCareerDto getCareer(String uid);
    void updateCareer(String uid, UserCareerUpdateReq req);
}
