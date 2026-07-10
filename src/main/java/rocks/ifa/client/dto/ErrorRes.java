package rocks.ifa.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public record ErrorRes(
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    OffsetDateTime timestamp,
    int status,
    String error,
    String message,
    String path
) {
    public ErrorRes(HttpStatus httpStatus, String message, String path) {
        this(
            OffsetDateTime.now(),
            httpStatus.value(),
            httpStatus.getReasonPhrase(),
            message,
            path
        );
    }
}
