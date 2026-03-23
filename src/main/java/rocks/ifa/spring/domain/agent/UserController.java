package rocks.ifa.spring.domain.agent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.client.UserService;
import rocks.ifa.spring.domain.clientCareer.ClientCareerService;
import rocks.ifa.spring.domain.clientCareer.ClientCareerUpdateReq;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceService;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceUpdateReq;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionService;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionUpdateReq;
import rocks.ifa.spring.domain.clientProfile.ClientProfileService;
import rocks.ifa.spring.domain.clientProfile.ClientProfileUpdateReq;
import rocks.ifa.spring.domain.clientTax.ClientTaxService;
import rocks.ifa.spring.domain.clientTax.ClientTaxUpdateReq;
import rocks.ifa.spring.infra.SecurityUtils;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "使用者核心資料管理")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ClientProfileService userProfileService;
    private final ClientCareerService userCareerService;
    private final ClientLaborPensionService userLaborPensionService;
    private final ClientLaborInsuranceService userLaborInsuranceService;
    private final ClientTaxService userTaxService;

    @Operation(summary = "註冊新使用者")
    @PostMapping("/")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationReq req) {
        userService.registerUser(req);
        return ResponseEntity.ok("註冊成功");
    }

    @Operation(summary = "獲取當前使用者所有資料")
    @GetMapping("/")
    public ResponseEntity<UserFullDataRes> getMe() {
        String uid = SecurityUtils.getCurrentUserUid();
        return ResponseEntity.ok(userService.getFullUserData(uid));
    }

    @Operation(summary = "更新使用者個人資料")
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody @Valid ClientProfileUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        userProfileService.updateProfile(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新使用者職涯資料")
    @PutMapping("/career")
    public ResponseEntity<String> updateCareer(@RequestBody @Valid ClientCareerUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        userCareerService.updateCareer(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新使用者勞退資料")
    @PutMapping("/labor-pension")
    public ResponseEntity<String> updateLaborPension(@RequestBody @Valid ClientLaborPensionUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        userLaborPensionService.updateLaborPension(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新使用者勞保資料")
    @PutMapping("/labor-insurance")
    public ResponseEntity<String> updateLaborInsurance(@RequestBody @Valid ClientLaborInsuranceUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        userLaborInsuranceService.updateLaborInsurance(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新使用者稅務資料")
    @PutMapping("/tax")
    public ResponseEntity<String> updateTax(@RequestBody @Valid ClientTaxUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        userTaxService.updateTax(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "刪除當前使用者帳號")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        String uid = SecurityUtils.getCurrentUserUid();
        userService.deleteUser(uid);
        return ResponseEntity.noContent().build();
    }
}
