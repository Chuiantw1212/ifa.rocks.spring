package rocks.ifa.spring.service;

import rocks.ifa.spring.dto.UserBusinessDto;
import rocks.ifa.spring.util.PageResponse;

public interface UserBusinessService {
    PageResponse<UserBusinessDto> getList(String uid, int currentPage, int pageSize);
    UserBusinessDto getById(String uid, Long id);
    UserBusinessDto create(String uid, UserBusinessDto req);
    UserBusinessDto update(String uid, Long id, UserBusinessDto req);
    void delete(String uid, Long id);
}
