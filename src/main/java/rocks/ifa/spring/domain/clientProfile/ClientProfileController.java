package rocks.ifa.spring.domain.clientProfile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.infra.security.SecurityUtils;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/profile")
@Tag(name = "Client Profile API", description = "管理客戶的個人基本資料")
@RequiredArgsConstructor
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

    @Operation(summary = "更新客戶個人資料")
    @PutMapping
    public ResponseEntity<String> updateProfile(@PathVariable Long clientId, @RequestBody @Valid ClientProfileContracts.UpdateProfileReq req) {
        String agentUid = SecurityUtils.getCurrentAgentUid();
        // The service should verify that the agent has permission for this client
        clientProfileService.updateProfile(String.valueOf(clientId), req);
        return ResponseEntity.ok("更新成功");
    }
}
