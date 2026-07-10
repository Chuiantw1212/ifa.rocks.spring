package rocks.ifa.spring.adapter.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.client.dto.PageResponse;
import rocks.ifa.spring.application.client.ClientService;
import rocks.ifa.spring.application.client.dtos.ClientFullDataRes;
import rocks.ifa.spring.application.client.dtos.CreateClientReq;
import rocks.ifa.spring.application.clientProfile.dtos.ProfileRes;
import rocks.ifa.spring.infrastructure.config.SecurityUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Client Aggregate API", description = "客戶資料聚合服務 (列表/單一查詢)")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "建立新客戶")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileRes createClient(@RequestBody @Valid CreateClientReq req) {
        String agentFirebaseUid = SecurityUtils.getCurrentUserUid();
        return clientService.createClient(req, agentFirebaseUid);
    }

    @Operation(summary = "獲取當前顧問的所有客戶列表 (分頁)")
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public PageResponse<ClientFullDataRes> listClients(Pageable pageable) {
        String agentUid = SecurityUtils.getCurrentUserUid();
        return clientService.listClientsByAgent(agentUid, pageable);
    }

    @Operation(summary = "獲取單一客戶的完整理財資料")
    @GetMapping("/{clientId}")
    @SecurityRequirement(name = "bearerAuth")
    public ClientFullDataRes getClient(@PathVariable UUID clientId) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        return clientService.getClientFullData(clientId, requesterUid);
    }

    @Operation(summary = "刪除客戶")
    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    public void deleteClient(@PathVariable UUID clientId) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        clientService.deleteClient(clientId, requesterUid);
    }
}
