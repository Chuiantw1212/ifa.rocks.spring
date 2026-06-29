package rocks.ifa.spring.domain.clientLaborPension;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.clientLaborPension.dtos.LaborPensionRes;
import rocks.ifa.spring.domain.clientLaborPension.dtos.UpdateLaborPensionReq;
import rocks.ifa.spring.infra.security.SecurityUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/labor-pension")
@Tag(name = "Client Sub: Labor Pension", description = "管理客戶的勞工退休金資料")
@RequiredArgsConstructor
public class ClientLaborPensionController {

    private final ClientLaborPensionService clientLaborPensionService;

    @GetMapping
    @Operation(summary = "獲取指定客戶的勞工退休金資料")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<LaborPensionRes> getLaborPension(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        LaborPensionRes laborPension = clientLaborPensionService.getLaborPension(clientId, requesterUid);
        return ResponseEntity.ok(laborPension);
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
