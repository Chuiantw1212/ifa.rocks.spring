package rocks.ifa.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.entity.UserLaborInsurance;
import rocks.ifa.spring.mapper.UserLaborInsuranceMapper;
import rocks.ifa.spring.dto.UserLaborInsuranceDto;
import rocks.ifa.spring.dto.UserLaborInsuranceUpdateReq;
import rocks.ifa.spring.service.UserLaborInsuranceService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLaborInsuranceServiceImpl implements UserLaborInsuranceService {

    private final UserLaborInsuranceMapper mapper;

    @Override
    public UserLaborInsuranceDto getLaborInsurance(String uid) {
        UserLaborInsurance entity = mapper.selectByUid(uid);
        if (entity == null) {
            log.warn("No labor insurance record found for UID: {}. Creating a minimal default record.", uid);
            return createDefaultLaborInsurance(uid);
        }
        return convertToDto(entity);
    }

    @Override
    @Transactional
    public void updateLaborInsurance(String uid, UserLaborInsuranceUpdateReq req) {
        boolean exists = mapper.existsByUid(uid);
        UserLaborInsurance entity;

        if (exists) {
            entity = mapper.selectByUid(uid);
        } else {
            log.info("No existing labor insurance for update, creating new one for UID: {}", uid);
            entity = new UserLaborInsurance();
            entity.setFirebaseUid(uid);
        }

        BeanUtils.copyProperties(req, entity);

        if (exists) {
            mapper.updateByUid(entity);
            log.info("✅ [LaborInsurance] Updated for user: {}", uid);
        } else {
            mapper.insert(entity);
            log.info("✅ [LaborInsurance] Created for user: {}", uid);
        }
    }

    @Transactional
    private UserLaborInsuranceDto createDefaultLaborInsurance(String uid) {
        UserLaborInsurance newInsurance = new UserLaborInsurance();
        newInsurance.setFirebaseUid(uid);
        mapper.insert(newInsurance);
        log.info("✅ Minimal default labor insurance record created for UID: {}", uid);
        return convertToDto(newInsurance);
    }

    private UserLaborInsuranceDto convertToDto(UserLaborInsurance entity) {
        UserLaborInsuranceDto dto = new UserLaborInsuranceDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
