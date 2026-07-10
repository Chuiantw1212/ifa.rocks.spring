package rocks.ifa.spring.application.metadata.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "預期壽命查詢結果")
public record LifeExpectancyRes(
    @Schema(description = "統計年份", example = "2025")
    Integer year,
    
    @Schema(description = "性別", example = "MALE")
    String gender,
    
    @Schema(description = "查詢年齡", example = "30")
    Integer age,
    
    @Schema(description = "預期總壽命 (歲)", example = "78.22")
    BigDecimal expectedLifespan
) {}
