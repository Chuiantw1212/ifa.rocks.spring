package rocks.ifa.spring.domain.clientLaborPension;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.clientLaborPension.dtos.LaborPensionRes;
import rocks.ifa.spring.domain.clientLaborPension.dtos.UpdateLaborPensionReq;
import rocks.ifa.spring.infra.security.SecurityUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/labor-pension")
@Tag(name = "Client Sub: Labor Pension", description = "管理客戶的勞工退休金資料")
@RequiredArgsConstructor
@Slf4j
public class ClientLaborPensionController {

    private final ClientLaborPensionService clientLaborPensionService;

    @GetMapping
    @Operation(summary = "獲取指定客戶的勞工退休金資料", description = "如果找不到資料，將回傳 200 OK 且 body 為一個空的 JSON 物件 {}。")
    @ApiResponse(responseCode = "200", description = "成功獲取資料或表示資料不存在。", content = {
            @Content(mediaType = "application/json", schema = @Schema(oneOf = {LaborPensionRes.class, Object.class}))
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Object> getLaborPension(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId) {
        
        String requesterUid = SecurityUtils.getCurrentUserUid();
        Optional<LaborPensionRes> laborPensionOpt = clientLaborPensionService.getLaborPension(clientId, requesterUid);
        
        if (laborPensionOpt.isPresent()) {
            return ResponseEntity.ok(laborPensionOpt.get());
        } else {
            return ResponseEntity.ok(Collections.emptyMap());
        }
    }

    @PutMapping
    @Operation(summary = "更新指定客戶的勞工退休金資料")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> updateLaborPension(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId,
            @RequestBody @Valid UpdateLaborPensionReq req) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        clientLaborPensionService.updateLaborPension(clientId, req, requesterUid);
        return ResponseEntity.noContent().build();
    }
}
