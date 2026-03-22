package rocks.ifa.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.entity.UserTax;
import rocks.ifa.spring.mapper.UserTaxMapper;
import rocks.ifa.spring.model.UserTaxDto;
import rocks.ifa.spring.model.UserTaxUpdateReq;
import rocks.ifa.spring.service.UserTaxService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTaxServiceImpl implements UserTaxService {

    private final UserTaxMapper userTaxMapper;

    @Override
    public UserTaxDto getTax(String uid) {
        UserTax entity = userTaxMapper.selectByUid(uid);
        if (entity == null) {
            log.warn("No tax record found for UID: {}. Creating a minimal default record.", uid);
            return createDefaultTax(uid);
        }
        return convertToDto(entity);
    }

    @Override
    @Transactional
    public void updateTax(String uid, UserTaxUpdateReq req) {
        boolean exists = userTaxMapper.existsByUid(uid);
        UserTax entity;

        if (exists) {
            entity = userTaxMapper.selectByUid(uid);
        } else {
            log.info("No existing tax record for update, creating new one for UID: {}", uid);
            entity = new UserTax();
            entity.setFirebaseUid(uid);
        }

        BeanUtils.copyProperties(req, entity);

        if (exists) {
            userTaxMapper.updateByUid(entity);
            log.info("✅ [Tax] Updated for user: {}", uid);
        } else {
            userTaxMapper.insert(entity);
            log.info("✅ [Tax] Created for user: {}", uid);
        }
    }

    @Transactional
    private UserTaxDto createDefaultTax(String uid) {
        UserTax newTax = new UserTax();
        newTax.setFirebaseUid(uid);
        userTaxMapper.insert(newTax);
        log.info("✅ Minimal default tax record created for UID: {}", uid);
        return convertToDto(newTax);
    }

    private UserTaxDto convertToDto(UserTax entity) {
        UserTaxDto dto = new UserTaxDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
