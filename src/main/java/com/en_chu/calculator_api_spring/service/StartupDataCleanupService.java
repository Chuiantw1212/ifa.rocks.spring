package com.en_chu.calculator_api_spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.en_chu.calculator_api_spring.mapper.DataAdminMapper;

@Service
public class StartupDataCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(StartupDataCleanupService.class);

    private final DataAdminMapper dataAdminMapper;

    public StartupDataCleanupService(DataAdminMapper dataAdminMapper) {
        this.dataAdminMapper = dataAdminMapper;
    }

    @Transactional
    public void cleanupOrphanedData() {
        logger.info("===== [Admin Task] Starting orphaned data cleanup... =====");

        int businessesDeleted = dataAdminMapper.deleteOrphanedBusinesses();
        if (businessesDeleted > 0) {
            logger.info("[Cleanup] Deleted {} orphaned records from 'user_businesses'.", businessesDeleted);
        }

        int creditCardsDeleted = dataAdminMapper.deleteOrphanedCreditCards();
        if (creditCardsDeleted > 0) {
            logger.info("[Cleanup] Deleted {} orphaned records from 'user_credit_cards'.", creditCardsDeleted);
        }

        int portfoliosDeleted = dataAdminMapper.deleteOrphanedPortfolios();
        if (portfoliosDeleted > 0) {
            logger.info("[Cleanup] Deleted {} orphaned records from 'user_portfolios'.", portfoliosDeleted);
        }

        int realEstatesDeleted = dataAdminMapper.deleteOrphanedRealEstates();
        if (realEstatesDeleted > 0) {
            logger.info("[Cleanup] Deleted {} orphaned records from 'user_real_estates'.", realEstatesDeleted);
        }

        logger.info("===== [Admin Task] Orphaned data cleanup finished. =====");
    }
}
