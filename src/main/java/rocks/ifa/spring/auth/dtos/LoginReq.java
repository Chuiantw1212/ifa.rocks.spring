package rocks.ifa.spring.auth.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(@NotBlank String firebaseToken) {
}
