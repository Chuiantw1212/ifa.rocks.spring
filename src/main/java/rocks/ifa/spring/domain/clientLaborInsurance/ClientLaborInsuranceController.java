package rocks.ifa.spring.domain.clientLaborInsurance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.clientLaborInsurance.dtos.LaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborInsurance.dtos.UpdateLaborInsuranceReq;
import rocks.ifa.spring.infra.security.SecurityUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/labor-insurance")
@Tag(name = "Client Sub: Labor Insurance", description = "管理客戶的勞工保險資料")
@RequiredArgsConstructor
public class ClientLaborInsuranceController {

    private final ClientLaborInsuranceService clientLaborInsuranceService;

    @GetMapping
    @Operation(summary = "獲取指定客戶的勞工保險資料", description = "如果找不到資料，將回傳 200 OK 且 body 為一個空的 JSON 物件 {}。")
    @ApiResponse(responseCode = "200", description = "成功獲取資料或表示資料不存在。", content = {
            @Content(mediaType = "application/json", schema = @Schema(oneOf = {LaborInsuranceRes.class, Object.class}))
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Object> getLaborInsurance(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId) {
        
        String requesterUid = SecurityUtils.getCurrentUserUid();
        Optional<LaborInsuranceRes> laborInsuranceOpt = clientLaborInsuranceService.getLaborInsurance(clientId, requesterUid);
        
        if (laborInsuranceOpt.isPresent()) {
            return ResponseEntity.ok(laborInsuranceOpt.get());
        } else {
            return ResponseEntity.ok(Collections.emptyMap());
        }
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
