package rocks.ifa.spring.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LiffIdToken(@JsonProperty("idToken") String idToken) {
}
