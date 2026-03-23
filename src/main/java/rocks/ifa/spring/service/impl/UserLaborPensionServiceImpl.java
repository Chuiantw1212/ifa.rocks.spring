package rocks.ifa.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.entity.UserLaborPension;
import rocks.ifa.spring.mapper.UserLaborPensionMapper;
import rocks.ifa.spring.dto.UserLaborPensionDto;
import rocks.ifa.spring.dto.UserLaborPensionUpdateReq;
import rocks.ifa.spring.service.UserLaborPensionService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLaborPensionServiceImpl implements UserLaborPensionService {

    private final UserLaborPensionMapper userLaborPensionMapper;

    @Override
    public UserLaborPensionDto getLaborPension(String uid) {
        UserLaborPension entity = userLaborPensionMapper.selectByUid(uid);
        if (entity == null) {
            log.warn("No labor pension record found for UID: {}. Creating a minimal default record.", uid);
            return createDefaultLaborPension(uid);
        }
        return convertToDto(entity);
    }

    @Override
    @Transactional
    public void updateLaborPension(String uid, UserLaborPensionUpdateReq req) {
        boolean exists = userLaborPensionMapper.existsByUid(uid);
        UserLaborPension entity;

        if (exists) {
            entity = userLaborPensionMapper.selectByUid(uid);
        } else {
            log.info("No existing labor pension for update, creating new one for UID: {}", uid);
            entity = new UserLaborPension();
            entity.setFirebaseUid(uid);
        }

        BeanUtils.copyProperties(req, entity);

        if (exists) {
            userLaborPensionMapper.updateByUid(entity);
            log.info("✅ [LaborPension] Updated for user: {}", uid);
        } else {
            userLaborPensionMapper.insert(entity);
            log.info("✅ [LaborPension] Created for user: {}", uid);
        }
    }

    @Transactional
    private UserLaborPensionDto createDefaultLaborPension(String uid) {
        UserLaborPension newPension = new UserLaborPension();
        newPension.setFirebaseUid(uid);
        userLaborPensionMapper.insert(newPension);
        log.info("✅ Minimal default labor pension record created for UID: {}", uid);
        return convertToDto(newPension);
    }

    private UserLaborPensionDto convertToDto(UserLaborPension entity) {
        UserLaborPensionDto dto = new UserLaborPensionDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
