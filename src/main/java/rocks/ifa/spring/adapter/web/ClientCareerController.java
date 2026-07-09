package rocks.ifa.spring.adapter.web;

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
import rocks.ifa.spring.application.clientCareer.ClientCareerService;
import rocks.ifa.spring.domain.clientCareer.dtos.CareerRes;
import rocks.ifa.spring.domain.clientCareer.dtos.UpdateCareerReq;
import rocks.ifa.spring.infrastructure.security.SecurityUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/career")
@Tag(name = "Client Sub: Career", description = "管理客戶的職涯與收入資料")
@RequiredArgsConstructor
@Slf4j
public class ClientCareerController {

    private final ClientCareerService clientCareerService;

    @GetMapping
    @Operation(summary = "獲取指定客戶的職涯資料", description = "如果找不到資料，將回傳 200 OK 且 body 為一個空的 JSON 物件 {}。")
    @ApiResponse(responseCode = "200", description = "成功獲取資料或表示資料不存在。", content = {
            @Content(mediaType = "application/json", schema = @Schema(oneOf = {CareerRes.class, Object.class}))
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Object> getCareer(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId) {
        
        String requesterUid = SecurityUtils.getCurrentUserUid();
        Optional<CareerRes> careerOpt = clientCareerService.getCareer(clientId, requesterUid);
        
        if (careerOpt.isPresent()) {
            return ResponseEntity.ok(careerOpt.get());
        } else {
            // CRITICAL FIX: Return a 200 OK with a non-null, empty JSON object body `{}`.
            // This prevents Spring MVC from triggering view resolution and throwing NoResourceFoundException.
            return ResponseEntity.ok(Collections.emptyMap());
        }
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
