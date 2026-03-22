package com.en_chu.calculator_api_spring.controller;

import com.en_chu.calculator_api_spring.service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
@Tag(name = "Status API", description = "提供伺服器狀態與健康檢查")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @Operation(summary = "獲取伺服器資源狀態", description = "回傳一個包含當前記憶體、CPU等核心指標的JSON物件。")
    @GetMapping
    public Map<String, Object> getServerStatus() {
        return statusService.getServerStatus();
    }
}
