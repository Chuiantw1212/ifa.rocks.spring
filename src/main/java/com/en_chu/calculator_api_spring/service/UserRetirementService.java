package com.en_chu.calculator_api_spring.service;

import com.en_chu.calculator_api_spring.entity.UserRetirement;
import com.en_chu.calculator_api_spring.mapper.UserRetirementMapper;
import com.en_chu.calculator_api_spring.model.UserRetirementDto;
import com.en_chu.calculator_api_spring.model.UserRetirementUpdateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRetirementService {

    private final UserRetirementMapper userRetirementMapper;

    public UserRetirementDto getRetirement(String uid) {
        UserRetirement entity = userRetirementMapper.selectByUid(uid);
        if (entity == null) {
            log.warn("No retirement record found for UID: {}. Creating a default record.", uid);
            return createDefaultRetirement(uid);
        }
        return convertToDto(entity);
    }

    @Transactional
    public void updateRetirement(String uid, UserRetirementUpdateReq req) {
        boolean exists = userRetirementMapper.existsByUid(uid);
        UserRetirement entity;

        if (exists) {
            entity = userRetirementMapper.selectByUid(uid);
        } else {
            log.info("No existing retirement for update, creating new one for UID: {}", uid);
            entity = new UserRetirement();
            entity.setFirebaseUid(uid);
        }

        BeanUtils.copyProperties(req, entity);

        if (exists) {
            userRetirementMapper.updateByUid(entity);
            log.info("✅ [Retirement] Updated for user: {}", uid);
        } else {
            userRetirementMapper.insert(entity);
            log.info("✅ [Retirement] Created for user: {}", uid);
        }
    }

    @Transactional
    public void patchRetirement(String uid, UserRetirementDto req) {
        boolean exists = userRetirementMapper.existsByUid(uid);
        UserRetirement entity;

        if (exists) {
            entity = userRetirementMapper.selectByUid(uid);
        } else {
            log.info("No existing retirement record for PATCH, creating a new one for UID: {}", uid);
            entity = new UserRetirement();
            entity.setFirebaseUid(uid);
            entity.setHouseholdType("single"); // Provide a default for the NOT NULL column
        }

        if (req.getHouseholdType() != null) entity.setHouseholdType(req.getHouseholdType());
        if (req.getHousingMode() != null) entity.setHousingMode(req.getHousingMode());
        if (req.getHousingCost() != null) entity.setHousingCost(req.getHousingCost());
        if (req.getHealthTierCode() != null) entity.setHealthTierCode(req.getHealthTierCode());
        if (req.getHealthCost() != null) entity.setHealthCost(req.getHealthCost());
        if (req.getActiveLivingCode() != null) entity.setActiveLivingCode(req.getActiveLivingCode());
        if (req.getActiveLivingCost() != null) entity.setActiveLivingCost(req.getActiveLivingCost());
        if (req.getSlowGoStartAge() != null) entity.setSlowGoStartAge(req.getSlowGoStartAge());
        if (req.getDefenseTierCode() != null) entity.setDefenseTierCode(req.getDefenseTierCode());
        if (req.getMonthlyMedicalCost() != null) entity.setMonthlyMedicalCost(req.getMonthlyMedicalCost());
        if (req.getCriticalIllnessCode() != null) entity.setCriticalIllnessCode(req.getCriticalIllnessCode());
        if (req.getCriticalIllnessReserve() != null) entity.setCriticalIllnessReserve(req.getCriticalIllnessReserve());
        if (req.getNogoStartAge() != null) entity.setNogoStartAge(req.getNogoStartAge());
        if (req.getLtcCareMode() != null) entity.setLtcCareMode(req.getLtcCareMode());
        if (req.getLtcMonthlyCost() != null) entity.setLtcMonthlyCost(req.getLtcMonthlyCost());
        if (req.getLtcMonthlySupplies() != null) entity.setLtcMonthlySupplies(req.getLtcMonthlySupplies());
        if (req.getLtcSubsidy() != null) entity.setLtcSubsidy(req.getLtcSubsidy());

        if (exists) {
            userRetirementMapper.updateByUid(entity);
            log.info("✅ [Retirement] Patched for user: {}", uid);
        } else {
            userRetirementMapper.insert(entity);
            log.info("✅ [Retirement] Created via PATCH for user: {}", uid);
        }
    }

    @Transactional
    private UserRetirementDto createDefaultRetirement(String uid) {
        UserRetirement newRetirement = new UserRetirement();
        newRetirement.setFirebaseUid(uid);
        newRetirement.setHouseholdType("single"); 
        
        userRetirementMapper.insert(newRetirement);
        log.info("✅ Default retirement record created for UID: {}", uid);
        return convertToDto(newRetirement);
    }

    private UserRetirementDto convertToDto(UserRetirement entity) {
        UserRetirementDto dto = new UserRetirementDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
