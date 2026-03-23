package rocks.ifa.spring.domain.agent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.infra.SecurityUtils;

@RestController
@RequestMapping("/api/v1/agent")
@Tag(name = "Agent API", description = "使用者帳號管理 (註冊/刪除)")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @Operation(summary = "註冊新使用者 (同步 Firebase Token)")
    @PostMapping("/")
    public ResponseEntity<String> registerAgent(@RequestBody @Valid AgentRegistrationReq req) {
        agentService.registerAgent(req);
        return ResponseEntity.ok("註冊成功");
    }

    @Operation(summary = "刪除當前使用者帳號及所有資料")
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteAgent() {
        String uid = SecurityUtils.getCurrentUserUid();
        agentService.deleteAgent(uid);
        return ResponseEntity.noContent().build();
    }
}
