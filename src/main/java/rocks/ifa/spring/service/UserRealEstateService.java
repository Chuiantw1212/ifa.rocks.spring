package rocks.ifa.spring.service;

import rocks.ifa.spring.dto.UserRealEstateDto;

import java.util.List;

public interface UserRealEstateService {
    UserRealEstateDto create(String uid, UserRealEstateDto req);
    List<UserRealEstateDto> getList(String uid);
    UserRealEstateDto getById(String uid, Long id);
    UserRealEstateDto update(String uid, Long id, UserRealEstateDto req);
    void delete(String uid, Long id);
}
