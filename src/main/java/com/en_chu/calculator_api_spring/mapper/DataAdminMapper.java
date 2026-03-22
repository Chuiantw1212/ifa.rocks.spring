package com.en_chu.calculator_api_spring.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 資料管理 Mapper，用於執行管理性質的 SQL 操作，例如資料清理。
 */
@Mapper
public interface DataAdminMapper {

    /**
     * 清除在 'usr_profiles' 中已不存在對應 UID 的孤兒企業資料。
     * @return 刪除的筆數
     */
    int deleteOrphanedBusinesses();

    /**
     * 清除在 'usr_profiles' 中已不存在對應 UID 的孤兒信用卡資料。
     * @return 刪除的筆數
     */
    int deleteOrphanedCreditCards();

    /**
     * 清除在 'usr_profiles' 中已不存在對應 UID 的孤兒投資組合資料。
     * @return 刪除的筆數
     */
    int deleteOrphanedPortfolios();

    /**
     * 清除在 'usr_profiles' 中已不存在對應 UID 的孤兒房地產資料。
     * @return 刪除的筆數
     */
    int deleteOrphanedRealEstates();
}