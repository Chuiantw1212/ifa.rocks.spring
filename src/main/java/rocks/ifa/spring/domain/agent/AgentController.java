package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agents")
@Tag(name = "Agent Management API", description = "IFA 顧問後台管理使用者帳號")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @Operation(summary = "顧問登入")
    @PostMapping("/login")
    public ResponseEntity<AgentContracts.AuthRes> login(@RequestBody @Valid AgentContracts.LoginReq req) {
        return ResponseEntity.ok(agentService.login(req));
    }

    @Operation(summary = "顧問登出")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String agentId = "dummy-agent-id"; 
        agentService.logout(agentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "建立新使用者")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgentContracts.AgentRes createAgent(@RequestBody @Valid AgentContracts.CreateAgentReq req) throws FirebaseAuthException {
        return agentService.createAgent(req);
    }

    @Operation(summary = "取得單一使用者資訊")
    @GetMapping("/{id}")
    public AgentContracts.AgentRes getAgent(@PathVariable String id) throws FirebaseAuthException {
        return agentService.getAgent(id);
    }

    @Operation(summary = "更新使用者資訊")
    @PutMapping("/{id}")
    public AgentContracts.AgentRes updateAgent(@PathVariable String id, @RequestBody @Valid AgentContracts.UpdateAgentReq req) throws FirebaseAuthException {
        return agentService.updateAgent(id, req);
    }

    @Operation(summary = "刪除使用者")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAgent(@PathVariable String id) throws FirebaseAuthException {
        agentService.deleteAgent(id);
    }
}
