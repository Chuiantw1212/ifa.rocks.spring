package rocks.ifa.spring.domain.clientProfile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.infra.security.SecurityUtils;
import rocks.ifa.spring.infra.common.PageResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client-profiles")
@Tag(name = "Client Profile API", description = "管理客戶的個人基本資料")
@RequiredArgsConstructor
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

    @Operation(summary = "獲取當前顧問的所有客戶基本資料列表 (分頁)")
    @GetMapping
    public PageResponse<ClientProfileContracts.ProfileRes> listClientProfiles(Pageable pageable) {
        String agentUid = SecurityUtils.getCurrentAgentUid();
        return clientProfileService.listClientProfilesByAgent(agentUid, pageable);
    }
    
    @Operation(summary = "獲取單一客戶的基本資料")
    @GetMapping("/{clientId}")
    public ClientProfileContracts.ProfileRes getClientProfile(@PathVariable UUID clientId) {
        // ...
        return clientProfileService.getClientProfileById(clientId);
    }

    @Operation(summary = "更新客戶個人資料 (PUT)")
    @PutMapping("/{clientId}")
    public ResponseEntity<String> updateProfile(@PathVariable UUID clientId, @RequestBody @Valid ClientProfileContracts.UpdateProfileReq req) {
        // ...
        clientProfileService.updateProfile(clientId.toString(), req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "部分更新客戶個人資料 (PATCH)")
    @PatchMapping("/{clientId}")
    public ResponseEntity<String> patchProfile(@PathVariable UUID clientId, @RequestBody ClientProfileContracts.PatchProfileReq req) {
        // The service should verify that the agent has permission for this client
        clientProfileService.patchProfile(clientId, req);
        return ResponseEntity.ok("部分更新成功");
    }
}
