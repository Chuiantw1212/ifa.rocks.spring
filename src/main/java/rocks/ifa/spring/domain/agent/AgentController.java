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
import rocks.ifa.spring.domain.agent.dtos.UpdateAgentReq;
import rocks.ifa.spring.domain.line.LineTokenPayload;

@RestController
@RequestMapping("/api/v1/agents")
@Tag(name = "Agent Management API", description = "管理 Agent (顧問) 的資源")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @Operation(summary = "Bind LINE account to the current user",
               description = "Binds a LINE identity to the currently authenticated user's agent profile.")
    @PostMapping("/me/bind-line")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "bearerAuth")
    public AgentRes bindLineUser(@RequestBody @Valid LineTokenPayload lineTokenPayload) {
        return agentService.bindLineUserToAgent(lineTokenPayload);
    }

    @Operation(summary = "Get a specific agent's profile")
    @GetMapping("/{id}")
    public AgentRes getAgent(@PathVariable String id) {
        return agentService.getAgent(id);
    }

    @Operation(summary = "Update a specific agent's profile")
    @PutMapping("/{id}")
    public AgentRes updateAgent(@PathVariable String id, @RequestBody @Valid UpdateAgentReq req) throws FirebaseAuthException {
        return agentService.updateAgent(id, req);
    }

    @Operation(summary = "Delete the current user's own agent account",
               description = "Deletes the agent account of the currently authenticated user.")
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    public void deleteOwnAgentAccount() throws FirebaseAuthException {
        agentService.deleteAgent();
    }
}
