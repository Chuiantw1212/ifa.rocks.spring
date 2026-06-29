package rocks.ifa.spring.domain.agent.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Ignore unknown properties from LINE's response to avoid deserialization errors
@JsonIgnoreProperties(ignoreUnknown = true)
public record LineVerifyResponse(
    String sub, // The User ID for which the ID token was issued.
    String name,
    String picture,
    String email
) {}
