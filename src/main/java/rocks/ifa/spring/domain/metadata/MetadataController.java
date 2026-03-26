package rocks.ifa.spring.domain.metadata;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.metadata.contracts.LifeExpectancyRes;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/metadata")
@Tag(name = "Metadata API", description = "通用元數據與工具查詢")
@RequiredArgsConstructor
public class MetadataController {

    private final MetadataService metadataService;

    @Operation(summary = "獲取所有前端配置元數據")
    @GetMapping
    public Map<String, Object> fetchAll() {
        return metadataService.getAllMetadata();
    }

    @Operation(summary = "查詢預期壽命")
    @GetMapping("/life-expectancy")
    public ResponseEntity<LifeExpectancyRes> getLifeExpectancy(
            @Parameter(description = "年份", example = "2025") @RequestParam Integer year,
            @Parameter(description = "性別 (MALE/FEMALE)", example = "MALE") @RequestParam String gender,
            @Parameter(description = "目前年齡", example = "30") @RequestParam Integer age) {
        
        if (age < 0 || age > 150) {
            return ResponseEntity.badRequest().build();
        }

        LifeExpectancyRes result = metadataService.getLifeExpectancy(year, gender, age);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/sync")
    @Operation(summary = "同步所有本地 metadata JSON 檔案到 Firestore (排除生命表)")
    public ResponseEntity<String> syncMetadata() {
        try {
            metadataService.syncMetadata();
            return ResponseEntity.ok("Metadata sync process started successfully.");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to read metadata files: " + e.getMessage());
        }
    }

    @PostMapping("/sync/life-table")
    @Operation(summary = "同步生命表 (Life Table) 到 Firestore", description = "專門用於上傳 `opt_life_table.json`，使用批次寫入以提升性能。")
    public ResponseEntity<String> syncLifeTable() {
        try {
            metadataService.syncLifeTable();
            return ResponseEntity.ok("Life table sync process started successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to sync life table: " + e.getMessage());
        }
    }
}
