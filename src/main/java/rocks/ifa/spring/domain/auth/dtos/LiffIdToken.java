package rocks.ifa.spring.domain.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LiffIdToken(@JsonProperty("idToken") String idToken) {
}
