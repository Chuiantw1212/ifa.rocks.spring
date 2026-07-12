package rocks.ifa.spring.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.common.dto.ErrorRes;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorRes> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        log.warn("ResponseStatusException caught: {} {} - {}", status.value(), request.getMethod(), request.getRequestURI());
        ErrorRes errorResponse = new ErrorRes(status, ex.getReason(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRes> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String firstErrorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("⚠️ DTO validation failed for request {}: {}", request.getRequestURI(), firstErrorMessage);
        String userMessage = "The request data is invalid. Please check the fields. Details: " + firstErrorMessage;
        ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, userMessage, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleAllUncaughtException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String path = request.getRequestURI();
        log.error("🔥🔥🔥 Uncaught Exception: {} {} - Root cause: {}", status.value(), path, ex.getMessage(), ex);
        String message = "An unexpected internal server error occurred.";
        if ("dev".equalsIgnoreCase(activeProfile)) {
            message = ex.getMessage();
        }
        ErrorRes errorResponse = new ErrorRes(status, message, path);
        return new ResponseEntity<>(errorResponse, status);
    }
}
