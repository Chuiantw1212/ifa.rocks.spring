package com.en_chu.calculator_api_spring.controller;

import com.en_chu.calculator_api_spring.model.LifeExpectancyRes;
import com.en_chu.calculator_api_spring.service.LifeExpectancyService;
import com.en_chu.calculator_api_spring.service.MetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/metadata")
@Tag(name = "Metadata API", description = "通用元數據與工具查詢")
@RequiredArgsConstructor // ✅ 使用 Lombok 實現建構函式注入
public class MetadataController {

    private final MetadataService metadataService; // ✅ 改為 private final
    private final LifeExpectancyService lifeExpectancyService; // ✅ 改為 private final

    @Operation(summary = "獲取所有前端配置元數據", description = "一次性獲取所有用於 UI 選項、配置的元數據，例如性別選項、財務配置等。")
    @GetMapping
    public Map<String, Object> fetchAll() {
        return metadataService.getAllMetadata();
    }

    @Operation(summary = "查詢預期壽命", description = "根據年份、性別、當前年齡，查詢對應的平均餘命。")
    @GetMapping("/life-expectancy")
    public ResponseEntity<LifeExpectancyRes> getLifeExpectancy(
            @Parameter(description = "年份", example = "2025") @RequestParam Integer year,
            @Parameter(description = "性別 (MALE/FEMALE)", example = "MALE") @RequestParam String gender,
            @Parameter(description = "目前年齡", example = "30") @RequestParam Integer age) {
        if (age < 0 || age > 150) {
            return ResponseEntity.badRequest().build();
        }

        LifeExpectancyRes result = lifeExpectancyService.getLifeExpectancy(year, gender, age);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}
