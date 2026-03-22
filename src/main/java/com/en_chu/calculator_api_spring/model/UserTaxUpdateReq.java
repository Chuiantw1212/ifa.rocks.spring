package com.en_chu.calculator_api_spring.model;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 專門用於更新使用者稅務資料的請求 DTO。
 */
@Data
public class UserTaxUpdateReq {

    /**
     * 預估其他所得 (Other Income) 包含：股利、利息、租金、兼職等需併入綜所稅的金額
     */
    private BigDecimal estimatedOtherIncome;

}
