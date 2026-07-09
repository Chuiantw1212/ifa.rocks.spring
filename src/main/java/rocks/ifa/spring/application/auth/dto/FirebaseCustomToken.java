package rocks.ifa.spring.application.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FirebaseCustomToken(@JsonProperty("customToken") String customToken) {
}
