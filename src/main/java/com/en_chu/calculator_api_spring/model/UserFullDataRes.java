package com.en_chu.calculator_api_spring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "使用者完整聚合資料 (Aggregation)")
public class UserFullDataRes {

    @Schema(description = "系統內部 ID (對應 Profile ID)")
    private Long id;

    @Schema(description = "基本資料")
    private UserProfileDto profile;

    @Schema(description = "職涯收入")
    private UserCareerDto career;
    
    @Schema(description = "勞工退休金設定")
    private UserLaborPensionDto laborPension;
    
    @Schema(description = "勞工保險金設定")
    private UserLaborInsuranceDto laborInsurance;
    
    @Schema(description = "退休")
    private UserRetirementDto retirement; 
    
    @Schema(description = "稅務規劃")
    private UserTaxDto tax; 
}