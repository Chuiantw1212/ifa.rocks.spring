package rocks.ifa.spring.domain.clientProfile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.clientProfile.contracts.PatchProfileReq;
import rocks.ifa.spring.domain.clientProfile.contracts.ProfileRes;
import rocks.ifa.spring.domain.clientProfile.contracts.UpdateProfileReq;
import rocks.ifa.spring.infra.common.PageResponse;
import rocks.ifa.spring.infra.security.SecurityUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client-profiles")
@Tag(name = "Client Profile API", description = "管理客戶的個人基本資料")
@RequiredArgsConstructor
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

    @Operation(summary = "[客戶專用] 獲取自己的個人基本資料")
    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ProfileRes getOwnProfile() {
        String clientFirebaseUid = SecurityUtils.getCurrentUserUid();
        return clientProfileService.getOwnProfile(clientFirebaseUid);
    }

    @Operation(summary = "[顧問專用] 獲取當前顧問的所有客戶基本資料列表 (分頁)")
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public PageResponse<ProfileRes> listClientProfiles(Pageable pageable) {
        String agentUid = SecurityUtils.getCurrentUserUid(); // Corrected to use the unified method
        return clientProfileService.listClientProfilesByAgent(agentUid, pageable);
    }

    @Operation(summary = "[顧問專用] 獲取單一客戶的基本資料")
    @GetMapping("/{clientId}")
    @SecurityRequirement(name = "bearerAuth")
    public ProfileRes getClientProfile(@PathVariable UUID clientId) {
        return clientProfileService.getClientProfileById(clientId);
    }

    @Operation(summary = "更新客戶個人資料 (PUT)")
    @PutMapping("/{clientId}")
    @SecurityRequirement(name = "bearerAuth")
    public ProfileRes updateProfile(@PathVariable UUID clientId, @RequestBody @Valid UpdateProfileReq req) {
        return clientProfileService.updateProfile(clientId, req);
    }

    @Operation(summary = "部分更新客戶個人資料 (PATCH)")
    @PatchMapping("/{clientId}")
    @SecurityRequirement(name = "bearerAuth")
    public ProfileRes patchProfile(@PathVariable UUID clientId, @RequestBody @Valid PatchProfileReq req) {
        return clientProfileService.patchProfile(clientId, req);
    }
}
