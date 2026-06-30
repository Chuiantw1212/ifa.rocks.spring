package rocks.ifa.spring.domain.clientLaborPension;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/labor-pension")
@Tag(name = "Client Sub: Labor Pension", description = "管理客戶的勞工退休金資料")
@RequiredArgsConstructor
@Slf4j
public class ClientLaborPensionController {

    private final ClientLaborPensionService clientLaborPensionService;

    @GetMapping
    @Operation(summary = "獲取指定客戶的勞工退休金資料")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<LaborPensionRes> getLaborPension(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId) {
        log.info("--- Received GET request for labor pension data for client ID: {} ---", clientId);
        try {
            String requesterUid = SecurityUtils.getCurrentUserUid();
            log.info("Requester UID: {}", requesterUid);
            LaborPensionRes laborPension = clientLaborPensionService.getLaborPension(clientId, requesterUid);
            log.info("--- Successfully retrieved labor pension data for client ID: {} ---", clientId);
            return ResponseEntity.ok(laborPension);
        } catch (Exception e) {
            log.error("❌❌❌ Unhandled exception in getLaborPension controller for client ID: {}", clientId, e);
            // Re-throwing the exception to let the global exception handler deal with it,
            // but we have logged the root cause here.
            throw e;
        }
    }

    @PutMapping
    @Operation(summary = "更新指定客戶的勞工退休金資料")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> updateLaborPension(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId,
            @RequestBody @Valid UpdateLaborPensionReq req) {
        log.info("--- Received PUT request to update labor pension data for client ID: {} ---", clientId);
        try {
            String requesterUid = SecurityUtils.getCurrentUserUid();
            log.info("Requester UID: {}", requesterUid);
            clientLaborPensionService.updateLaborPension(clientId, req, requesterUid);
            log.info("--- Successfully updated labor pension data for client ID: {} ---", clientId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("❌❌❌ Unhandled exception in updateLaborPension controller for client ID: {}", clientId, e);
            throw e;
        }
    }
}
