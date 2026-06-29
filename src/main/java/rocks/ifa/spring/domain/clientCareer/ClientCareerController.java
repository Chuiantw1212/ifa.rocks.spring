package rocks.ifa.spring.domain.clientCareer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.clientCareer.contracts.CareerRes;
import rocks.ifa.spring.domain.clientCareer.contracts.UpdateCareerReq;
import rocks.ifa.spring.infra.security.SecurityUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/career")
@Tag(name = "Client Sub: Career", description = "管理客戶的職涯與收入資料")
@RequiredArgsConstructor
public class ClientCareerController {

    private final ClientCareerService clientCareerService;

    @GetMapping
    @Operation(summary = "獲取指定客戶的職涯資料")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CareerRes> getCareer(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        CareerRes career = clientCareerService.getCareer(clientId, requesterUid);
        return ResponseEntity.ok(career);
    }

    @PutMapping
    @Operation(summary = "更新指定客戶的職涯資料")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> updateCareer(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId,
            @RequestBody @Valid UpdateCareerReq req) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        clientCareerService.updateCareer(clientId, req, requesterUid);
        return ResponseEntity.noContent().build();
    }
}
