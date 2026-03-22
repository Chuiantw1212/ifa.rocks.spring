package com.en_chu.calculator_api_spring.service;

import com.en_chu.calculator_api_spring.entity.UserProfile;
import com.en_chu.calculator_api_spring.mapper.UserProfileMapper;
import com.en_chu.calculator_api_spring.model.UserProfileDto;
import com.en_chu.calculator_api_spring.model.UserProfileUpdateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileMapper userProfileMapper;

    public UserProfileDto getProfile(String uid) {
        UserProfile entity = userProfileMapper.selectByUid(uid);
        if (entity == null) {
            log.warn("No profile found for UID: {}. Creating a minimal default profile.", uid);
            return createDefaultProfile(uid);
        }
        return convertToDto(entity);
    }

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
