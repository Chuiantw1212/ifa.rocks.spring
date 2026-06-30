package rocks.ifa.spring.domain.clientLaborInsurance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.clientLaborInsurance.dtos.LaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborInsurance.dtos.UpdateLaborInsuranceReq;
import rocks.ifa.spring.infra.security.SecurityUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/labor-insurance")
@Tag(name = "Client Sub: Labor Insurance", description = "管理客戶的勞工保險資料")
@RequiredArgsConstructor
public class ClientLaborInsuranceController {

    private final ClientLaborInsuranceService clientLaborInsuranceService;

    @GetMapping
    @Operation(summary = "獲取指定客戶的勞工保險資料")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<LaborInsuranceRes> getLaborInsurance(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        LaborInsuranceRes laborInsurance = clientLaborInsuranceService.getLaborInsurance(clientId, requesterUid);
        return ResponseEntity.ok(laborInsurance);
    }

    @PutMapping
    @Operation(summary = "更新指定客戶的勞工保險資料")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> updateLaborInsurance(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId,
            @RequestBody @Valid UpdateLaborInsuranceReq req) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        clientLaborInsuranceService.updateLaborInsurance(clientId, req, requesterUid);
        return ResponseEntity.noContent().build();
    }
}
