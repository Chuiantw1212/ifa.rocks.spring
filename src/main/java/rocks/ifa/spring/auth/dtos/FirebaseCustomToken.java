package rocks.ifa.spring.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FirebaseCustomToken(@JsonProperty("customToken") String customToken) {
}
