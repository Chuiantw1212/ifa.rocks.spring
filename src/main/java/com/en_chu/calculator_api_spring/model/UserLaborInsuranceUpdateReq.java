package com.en_chu.calculator_api_spring.model;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO for updating user labor insurance data.
 * This DTO contains all fields that can be updated by the user or recalculated by the server.
 */
@Data
public class UserLaborInsuranceUpdateReq {

    private Integer expectedClaimAge;
    private BigDecimal averageMonthlySalary;
    private Integer insuranceSeniority;
    private BigDecimal predictedRemainingLife;
    private BigDecimal predictedMonthlyAnnuity;
    
}
