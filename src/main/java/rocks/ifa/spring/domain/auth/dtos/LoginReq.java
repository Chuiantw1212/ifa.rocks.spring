package rocks.ifa.spring.domain.auth.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(@NotBlank String firebaseToken) {
}
