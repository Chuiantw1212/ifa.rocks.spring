package rocks.ifa.spring.domain.clientProfile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.clientProfile.dtos.PatchProfileReq;
import rocks.ifa.spring.domain.clientProfile.dtos.ProfileRes;
import rocks.ifa.spring.domain.clientProfile.dtos.UpdateProfileReq;
import rocks.ifa.spring.infrastructure.common.PageResponse;
import rocks.ifa.spring.infrastructure.security.SecurityUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client/profiles") // Reverted
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
        String agentUid = SecurityUtils.getCurrentUserUid();
        return clientProfileService.listClientProfilesByAgent(agentUid, pageable);
    }

    @Operation(summary = "[顧問/客戶] 獲取單一客戶的基本資料 (需權限)")
    @GetMapping("/{clientId}")
    @SecurityRequirement(name = "bearerAuth")
    public ProfileRes getClientProfile(@PathVariable UUID clientId) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        return clientProfileService.getClientProfileById(clientId, requesterUid);
    }

    @Operation(summary = "更新客戶個人資料 (PUT)")
    @PutMapping("/{clientId}")
    @SecurityRequirement(name = "bearerAuth")
    public ProfileRes updateProfile(@PathVariable UUID clientId, @RequestBody @Valid UpdateProfileReq req) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        return clientProfileService.updateProfile(clientId, req, requesterUid);
    }

    @Operation(summary = "部分更新客戶個人資料 (PATCH)")
    @PatchMapping("/{clientId}")
    @SecurityRequirement(name = "bearerAuth")
    public ProfileRes patchProfile(@PathVariable UUID clientId, @RequestBody @Valid PatchProfileReq req) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        return clientProfileService.patchProfile(clientId, req, requesterUid);
    }
}
