package rocks.ifa.spring.domain.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FirebaseCustomToken(@JsonProperty("customToken") String customToken) {
}
