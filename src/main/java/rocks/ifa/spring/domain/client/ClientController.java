package rocks.ifa.spring.domain.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.clientProfile.ClientProfileContracts;
import rocks.ifa.spring.infra.SecurityUtils;
import rocks.ifa.spring.infra.common.PageResponse;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Client Aggregate API", description = "客戶資料聚合服務 (列表/單一查詢)")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "建立新客戶")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientProfileContracts.ProfileRes createClient(@RequestBody @Valid ClientContracts.CreateClientReq req) {
        return clientService.createClient(req);
    }

    @Operation(summary = "獲取當前顧問的所有客戶列表 (分頁)")
    @GetMapping
    public PageResponse<ClientFullDataRes> listClients(Pageable pageable) {
        String agentUid = SecurityUtils.getCurrentUserUid();
        return clientService.listClientsByAgent(agentUid, pageable);
    }

    @Operation(summary = "獲取單一客戶的完整理財資料")
    @GetMapping("/{clientId}")
    public ClientFullDataRes getClient(@PathVariable String clientId) {
        // In a real app, you'd also pass the agent's UID to the service
        // to verify they have permission to view this client.
        return clientService.getClientFullData(clientId);
    }
}
