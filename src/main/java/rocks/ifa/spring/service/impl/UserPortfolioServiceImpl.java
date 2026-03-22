package rocks.ifa.spring.service.impl;

import rocks.ifa.spring.entity.UserPortfolio;
import rocks.ifa.spring.mapper.UserPortfolioMapper;
import rocks.ifa.spring.model.UserPortfolioCreateReq;
import rocks.ifa.spring.model.UserPortfolioDto;
import rocks.ifa.spring.model.UserPortfolioUpdateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.service.UserPortfolioService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPortfolioServiceImpl implements UserPortfolioService {

    private final UserPortfolioMapper mapper;

    @Override
    public List<UserPortfolioDto> getList(String uid) {
        return mapper.selectByUid(uid).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserPortfolioDto getById(String uid, Long id) {
        UserPortfolio entity = mapper.selectByIdAndUid(id, uid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到該投資部位"));
        return convertToDto(entity);
    }

    @Override
    @Transactional
    public UserPortfolioDto create(String uid, UserPortfolioCreateReq req) {
        UserPortfolio entity = new UserPortfolio();

        entity.setCountryCode(req.getCountryCode());
        entity.setCurrency(req.getCurrency());
        entity.setExchangeRate(req.getExchangeRate());
        entity.setMarketValue(req.getMarketValue());
        
        entity.setRealizedPnl(BigDecimal.ZERO);

        entity.setFirebaseUid(uid);

        mapper.insert(entity);
        log.info("Created portfolio id={} for user={}", entity.getId(), uid);

        return convertToDto(entity);
    }

    @Override
    @Transactional
    public UserPortfolioDto update(String uid, Long id, UserPortfolioUpdateReq req) {
        UserPortfolio entity = mapper.selectByIdAndUid(id, uid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "更新失敗：找不到該資料或無權限"));

        entity.setCountryCode(req.getCountryCode());
        entity.setCurrency(req.getCurrency());
        entity.setExchangeRate(req.getExchangeRate());
        entity.setMarketValue(req.getMarketValue());
        entity.setRealizedPnl(req.getRealizedPnl());

        mapper.update(entity);

        return convertToDto(entity);
    }

    @Override
    @Transactional
    public void delete(String uid, Long id) {
        int rows = mapper.deleteByIdAndUid(id, uid);
        if (rows == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "刪除失敗：找不到該資料或無權限");
        }
    }

    private UserPortfolioDto convertToDto(UserPortfolio entity) {
        UserPortfolioDto dto = new UserPortfolioDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
