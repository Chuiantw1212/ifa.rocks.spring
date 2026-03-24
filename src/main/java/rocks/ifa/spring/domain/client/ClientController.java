package rocks.ifa.spring.domain.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.clientCareer.ClientCareerContracts;
import rocks.ifa.spring.domain.clientCareer.ClientCareerService;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceContracts;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceService;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionContracts;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionService;
import rocks.ifa.spring.domain.clientProfile.ClientProfileContracts;
import rocks.ifa.spring.domain.clientProfile.ClientProfileService;
import rocks.ifa.spring.domain.clientTax.ClientTaxContracts;
import rocks.ifa.spring.domain.clientTax.ClientTaxService;
import rocks.ifa.spring.infra.SecurityUtils;
import rocks.ifa.spring.infra.common.PageResponse;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Client API", description = "客戶理財檔案管理")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientProfileService clientProfileService;
    private final ClientCareerService clientCareerService;
    private final ClientLaborPensionService clientLaborPensionService;
    private final ClientLaborInsuranceService clientLaborInsuranceService;
    private final ClientTaxService clientTaxService;

    @Operation(summary = "建立新客戶")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientProfileContracts.ProfileRes createClient(@RequestBody @Valid ClientContracts.CreateClientReq req) {
        return clientService.createClient(req);
    }

    @Operation(summary = "獲取當前客戶所有理財資料")
    @GetMapping("/")
    public ResponseEntity<ClientFullDataRes> getClientData() {
        String uid = SecurityUtils.getCurrentUserUid();
        return ResponseEntity.ok(clientService.getClientFullData(uid));
    }

    @Operation(summary = "更新客戶個人資料")
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody @Valid ClientProfileContracts.UpdateProfileReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        clientProfileService.updateProfile(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新客戶職涯資料")
    @PutMapping("/career")
    public ResponseEntity<String> updateCareer(@RequestBody @Valid ClientCareerContracts.UpdateCareerReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        clientCareerService.updateCareer(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新客戶勞退資料")
    @PutMapping("/labor-pension")
    public ResponseEntity<String> updateLaborPension(@RequestBody @Valid ClientLaborPensionContracts.UpdateLaborPensionReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        clientLaborPensionService.updateLaborPension(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新客戶勞保資料")
    @PutMapping("/labor-insurance")
    public ResponseEntity<String> updateLaborInsurance(@RequestBody @Valid ClientLaborInsuranceContracts.UpdateLaborInsuranceReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        clientLaborInsuranceService.updateLaborInsurance(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新客戶稅務資料")
    @PutMapping("/tax")
    public ResponseEntity<String> updateTax(@RequestBody @Valid ClientTaxContracts.UpdateTaxReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        clientTaxService.updateTax(uid, req);
        return ResponseEntity.ok("更新成功");
    }
}
