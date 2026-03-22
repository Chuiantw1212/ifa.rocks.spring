package rocks.ifa.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.entity.UserCareer;
import rocks.ifa.spring.mapper.UserCareerMapper;
import rocks.ifa.spring.model.UserCareerDto;
import rocks.ifa.spring.model.UserCareerUpdateReq;
import rocks.ifa.spring.service.UserCareerService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCareerServiceImpl implements UserCareerService {

    private final UserCareerMapper userCareerMapper;

    @Override
    public UserCareerDto getCareer(String uid) {
        UserCareer entity = userCareerMapper.selectByUid(uid);
        if (entity == null) {
            log.warn("No career record found for UID: {}. Creating a minimal default record.", uid);
            return createDefaultCareer(uid);
        }
        return convertToDto(entity);
    }

    @Override
    @Transactional
    public void updateCareer(String uid, UserCareerUpdateReq req) {
        boolean exists = userCareerMapper.existsByUid(uid);
        UserCareer entity;

        if (exists) {
            entity = userCareerMapper.selectByUid(uid);
        } else {
            log.info("No existing career for update, creating new one for UID: {}", uid);
            entity = new UserCareer();
            entity.setFirebaseUid(uid);
        }

        BeanUtils.copyProperties(req, entity);

        if (exists) {
            userCareerMapper.updateByUid(entity);
            log.info("✅ [Career] Updated for user: {}", uid);
        } else {
            userCareerMapper.insert(entity);
            log.info("✅ [Career] Created for user: {}", uid);
        }
    }

    @Transactional
    private UserCareerDto createDefaultCareer(String uid) {
        UserCareer newCareer = new UserCareer();
        newCareer.setFirebaseUid(uid);
        userCareerMapper.insert(newCareer);
        log.info("✅ Minimal default career record created for UID: {}", uid);
        return convertToDto(newCareer);
    }

    private UserCareerDto convertToDto(UserCareer entity) {
        UserCareerDto dto = new UserCareerDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
