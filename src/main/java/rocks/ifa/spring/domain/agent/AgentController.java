package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.agent.dtos.AgentRes;
import rocks.ifa.spring.domain.agent.dtos.CreateAgentReq;
import rocks.ifa.spring.domain.agent.dtos.UpdateAgentReq;
import rocks.ifa.spring.domain.line.LineTokenPayload;

@RestController
@RequestMapping("/api/v1/agents")
@Tag(name = "Agent Management API", description = "IFA 顧問後台管理使用者帳號")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @Operation(summary = "將 LINE 使用者綁定到當前登入的 Firebase 使用者",
               description = "此端點應在 LINE 登入流程完成後，由前端呼叫。它會將 LINE ID Token 中的使用者資訊與當前 Firebase 使用者建立關聯。")
    @PostMapping("/bind-line")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "bearerAuth")
    public AgentRes bindLineUser(@RequestBody @Valid LineTokenPayload lineTokenPayload) {
        return agentService.bindLineUserToAgent(lineTokenPayload);
    }

    @Operation(summary = "建立新顧問")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgentRes createAgent(@RequestBody @Valid CreateAgentReq req) throws FirebaseAuthException {
        return agentService.createAgent(req);
    }

    @Operation(summary = "取得單一顧問資訊")
    @GetMapping("/{id}")
    public AgentRes getAgent(@PathVariable String id) {
        return agentService.getAgent(id);
    }

    @Operation(summary = "更新顧問資訊")
    @PutMapping("/{id}")
    public AgentRes updateAgent(@PathVariable String id, @RequestBody @Valid UpdateAgentReq req) throws FirebaseAuthException {
        return agentService.updateAgent(id, req);
    }

    @Operation(summary = "刪除自己的顧問帳號", description = "刪除當前登入用戶自己的帳號，並連動刪除所有相關資料。")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    public void deleteOwnAgentAccount() throws FirebaseAuthException {
        agentService.deleteAgent();
    }
}
