package rocks.ifa.spring.domain.clientCareer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.domain.clientCareer.dtos.CareerRes;
import rocks.ifa.spring.domain.clientCareer.dtos.UpdateCareerReq;
import rocks.ifa.spring.infra.security.SecurityUtils;

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
    @Operation(summary = "獲取指定客戶的職涯資料", description = "如果找不到關聯的客戶資料或職涯資料，將回傳 200 OK 且 body 為空。")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CareerRes> getCareer(
            @Parameter(description = "客戶的唯一識別碼 (UUID)", required = true)
            @PathVariable UUID clientId) {
        
        try {
            String requesterUid = SecurityUtils.getCurrentUserUid();
            Optional<CareerRes> careerOpt = clientCareerService.getCareer(clientId, requesterUid);
            
            return careerOpt
                    .map(ResponseEntity::ok) // If present, wrap it in a 200 OK response
                    .orElse(ResponseEntity.ok().build()); // If career data is not found, return 200 OK with empty body

        } catch (ResponseStatusException ex) {
            // This specifically catches the case where the client profile itself is not found (404)
            // or when there's a permission issue (403).
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Client profile not found for ID {} during career fetch. Returning 200 with empty body.", clientId);
                return ResponseEntity.ok().build();
            }
            // For other status exceptions (like 403 Forbidden), re-throw to be handled by the global handler.
            throw ex;
        } catch (Exception e) {
            // Catch any other unexpected exceptions from the service layer
            log.error("An unexpected error occurred in getCareer controller for client ID: {}", clientId, e);
            // Re-throw to let the GlobalExceptionHandler create a structured 500 response
            throw e;
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
