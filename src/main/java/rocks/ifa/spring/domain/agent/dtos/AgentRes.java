package rocks.ifa.spring.domain.agent.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents an agent user's public profile.")
public record AgentRes(
    @Schema(description = "The unique identifier of the agent.", example = "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6")
    String uid,

    @Schema(description = "The agent's email address.", example = "agent@example.com")
    String email,

    @Schema(description = "The agent's display name.", example = "John Doe")
    String displayName,

    @Schema(description = "URL of the agent's profile picture.", example = "https://example.com/profile.jpg")
    String picture
) {}
