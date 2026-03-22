package rocks.ifa.spring.service;

import rocks.ifa.spring.model.UserPortfolioCreateReq;
import rocks.ifa.spring.model.UserPortfolioDto;
import rocks.ifa.spring.model.UserPortfolioUpdateReq;

import java.util.List;

public interface UserPortfolioService {
    List<UserPortfolioDto> getList(String uid);
    UserPortfolioDto getById(String uid, Long id);
    UserPortfolioDto create(String uid, UserPortfolioCreateReq req);
    UserPortfolioDto update(String uid, Long id, UserPortfolioUpdateReq req);
    void delete(String uid, Long id);
}
