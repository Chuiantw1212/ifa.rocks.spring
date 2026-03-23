package rocks.ifa.spring.domain.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.clientCareer.ClientCareerService;
import rocks.ifa.spring.domain.clientCareer.UserCareerUpdateReq;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceService;
import rocks.ifa.spring.domain.clientLaborInsurance.UserLaborInsuranceUpdateReq;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionService;
import rocks.ifa.spring.domain.clientLaborPension.UserLaborPensionUpdateReq;
import rocks.ifa.spring.domain.clientProfile.ClientProfileService;
import rocks.ifa.spring.domain.clientProfile.UserProfileUpdateReq;
import rocks.ifa.spring.domain.clientTax.ClientTaxService;
import rocks.ifa.spring.domain.clientTax.UserTaxUpdateReq;
import rocks.ifa.spring.infra.util.SecurityUtils;

@RestController
@RequestMapping("/api/v1/client")
@Tag(name = "Client API", description = "客戶理財檔案管理")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientProfileService clientProfileService;
    private final ClientCareerService clientCareerService;
    private final ClientLaborPensionService clientLaborPensionService;
    private final ClientLaborInsuranceService clientLaborInsuranceService;
    private final ClientTaxService clientTaxService;

    @Operation(summary = "獲取當前客戶所有理財資料")
    @GetMapping("/")
    public ResponseEntity<ClientFullDataRes> getClientData() {
        String uid = SecurityUtils.getCurrentUserUid();
        return ResponseEntity.ok(clientService.getClientFullData(uid));
    }

    @Operation(summary = "更新客戶個人資料")
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody @Valid UserProfileUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        clientProfileService.updateProfile(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新客戶職涯資料")
    @PutMapping("/career")
    public ResponseEntity<String> updateCareer(@RequestBody @Valid UserCareerUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        clientCareerService.updateCareer(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新客戶勞退資料")
    @PutMapping("/labor-pension")
    public ResponseEntity<String> updateLaborPension(@RequestBody @Valid UserLaborPensionUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        clientLaborPensionService.updateLaborPension(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新客戶勞保資料")
    @PutMapping("/labor-insurance")
    public ResponseEntity<String> updateLaborInsurance(@RequestBody @Valid UserLaborInsuranceUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        clientLaborInsuranceService.updateLaborInsurance(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新客戶稅務資料")
    @PutMapping("/tax")
    public ResponseEntity<String> updateTax(@RequestBody @Valid UserTaxUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        clientTaxService.updateTax(uid, req);
        return ResponseEntity.ok("更新成功");
    }
}
