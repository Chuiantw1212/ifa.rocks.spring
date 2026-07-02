package rocks.ifa.spring.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record LineTokenPayload(
    @JsonProperty("iss") String iss,
    @JsonProperty("sub") String sub,
    @JsonProperty("aud") String aud,
    @JsonProperty("exp") long exp,
    @JsonProperty("iat") long iat,
    @JsonProperty("name") String name,
    @JsonProperty("picture") String picture
) {
    public void validate(String expectedAud) {
        if (!"https://access.line.me".equals(iss)) {
            throw new IllegalArgumentException("Invalid issuer: " + iss);
        }
        if (!expectedAud.equals(aud)) {
            throw new IllegalArgumentException("Invalid audience: " + aud);
        }
        if (Instant.now().getEpochSecond() > exp) {
            throw new IllegalArgumentException("Token has expired");
        }
    }
}
