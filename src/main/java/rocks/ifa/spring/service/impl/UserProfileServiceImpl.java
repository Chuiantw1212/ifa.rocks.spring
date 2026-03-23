package rocks.ifa.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.entity.UserProfile;
import rocks.ifa.spring.mapper.UserProfileMapper;
import rocks.ifa.spring.dto.UserProfileDto;
import rocks.ifa.spring.dto.UserProfileUpdateReq;
import rocks.ifa.spring.service.UserProfileService;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileDto getProfile(String uid) {
        UserProfile entity = userProfileMapper.selectByUid(uid);
        if (entity == null) {
            log.warn("No profile found for UID: {}. Creating a minimal default profile.", uid);
            return createDefaultProfile(uid);
        }
        return convertToDto(entity);
    }

    @Override
    @Transactional
    public void updateProfile(String uid, UserProfileUpdateReq req) {
        boolean exists = userProfileMapper.existsByUid(uid);
        UserProfile entity;

        if (exists) {
            entity = userProfileMapper.selectByUid(uid);
        } else {
            log.info("No existing profile for update, creating new one for UID: {}", uid);
            entity = new UserProfile();
            entity.setFirebaseUid(uid);
        }

        entity.setBirthDate(req.getBirthDate());
        entity.setGender(req.getGender());
        entity.setMarriageYear(req.getMarriageYear());
        entity.setBiography(req.getBiography());
        entity.setCareerInsuranceType(req.getCareerInsuranceType());

        if (req.getBirthDate() != null) {
            entity.setCurrentAge(Period.between(req.getBirthDate(), LocalDate.now()).getYears());
        } else if (!exists) {
            entity.setCurrentAge(null);
        }

        if (exists) {
            userProfileMapper.updateByUid(entity);
            log.info("✅ [Profile] Updated for user: {}", uid);
        } else {
            userProfileMapper.insert(entity);
            log.info("✅ [Profile] Created for user: {}", uid);
        }
    }

    @Transactional
    private UserProfileDto createDefaultProfile(String uid) {
        UserProfile newProfile = new UserProfile();
        newProfile.setFirebaseUid(uid);
        userProfileMapper.insert(newProfile);
        log.info("✅ Minimal default profile created for UID: {}", uid);
        return convertToDto(newProfile);
    }

    private UserProfileDto convertToDto(UserProfile entity) {
        UserProfileDto dto = new UserProfileDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
