package rocks.ifa.spring.adapter.web;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.application.agent.AgentService;
import rocks.ifa.spring.application.agent.dto.AgentRes;
import rocks.ifa.spring.application.agent.dto.UpdateAgentReq;
import rocks.ifa.spring.domain.line.LineTokenPayload;
import rocks.ifa.spring.infrastructure.config.SecurityUtils;

@RestController
@RequestMapping("/api/v1/agents")
@Tag(name = "Agent Management API", description = "管理 Agent (顧問) 的資源")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @Operation(summary = "Get the current user's own agent profile",
               description = "Retrieves the complete agent profile for the currently authenticated user.")
    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public AgentRes getOwnAgentProfile() {
        String firebaseUid = SecurityUtils.getCurrentUserUid();
        return agentService.getAgentByFirebaseUid(firebaseUid);
    }

    @Operation(summary = "Bind LINE account to the current user")
    @PostMapping("/me/bind-line")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "bearerAuth")
    public AgentRes bindLineUser(@RequestBody @Valid LineTokenPayload lineTokenPayload) {
        return agentService.bindLineUserToAgent(lineTokenPayload);
    }

    @Operation(summary = "Get a specific agent's profile by their internal UUID")
    @GetMapping("/{id}")
    public AgentRes getAgent(@PathVariable String id) {
        return agentService.getAgent(id);
    }

    @Operation(summary = "Update a specific agent's profile")
    @PutMapping("/{id}")
    public AgentRes updateAgent(@PathVariable String id, @RequestBody @Valid UpdateAgentReq req) throws FirebaseAuthException {
        return agentService.updateAgent(id, req);
    }

    @Operation(summary = "Delete the current user's own agent account")
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    public void deleteOwnAgentAccount() throws FirebaseAuthException {
        agentService.deleteAgent();
    }
}
