package rocks.ifa.spring.service;

import rocks.ifa.spring.dto.UserPortfolioCreateReq;
import rocks.ifa.spring.dto.UserPortfolioDto;
import rocks.ifa.spring.dto.UserPortfolioUpdateReq;

import java.util.List;

public interface UserPortfolioService {
    List<UserPortfolioDto> getList(String uid);
    UserPortfolioDto getById(String uid, Long id);
    UserPortfolioDto create(String uid, UserPortfolioCreateReq req);
    UserPortfolioDto update(String uid, Long id, UserPortfolioUpdateReq req);
    void delete(String uid, Long id);
}
