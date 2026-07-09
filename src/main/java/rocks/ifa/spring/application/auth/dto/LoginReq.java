package rocks.ifa.spring.application.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(@NotBlank String firebaseToken) {
}
