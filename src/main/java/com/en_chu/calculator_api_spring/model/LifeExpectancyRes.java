package com.en_chu.calculator_api_spring.model;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "預期壽命查詢結果")
public class LifeExpectancyRes {
    
    @Schema(description = "統計年份", example = "2025")
    private Integer year;
    
    @Schema(description = "性別", example = "MALE")
    private String gender;
    
    @Schema(description = "查詢年齡", example = "30")
    private Integer age;
    
    @Schema(description = "預期總壽命 (歲)", example = "78.22")
    private BigDecimal expectedLifespan;
}