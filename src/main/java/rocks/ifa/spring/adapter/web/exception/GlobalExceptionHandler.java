package rocks.ifa.spring.adapter.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import rocks.ifa.spring.client.dto.ErrorRes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    /**
     * CRITICAL FIX: Handles the specific case where a controller returns an empty body,
     * which can mistakenly trigger the static resource handler.
     * This handler intercepts the exception and returns the desired 200 OK with an empty body.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("✅ NoResourceFoundException caught for {}. This is likely due to a GET endpoint returning an empty body. Suppressing 500 and returning 200 OK as requested.", request.getRequestURI());
        return ResponseEntity.ok().build();
    }

    /**
     * Handles exceptions that we throw intentionally (e.g., for authorization).
     * Catches: 403 Forbidden, 404 Not Found, etc.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorRes> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        log.warn("ResponseStatusException caught: {} {} - {}", status.value(), request.getMethod(), request.getRequestURI());
        ErrorRes errorResponse = new ErrorRes(status, ex.getReason(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Handles DTO validation failures (@Valid).
     * Catches: 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRes> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String firstErrorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("⚠️ DTO validation failed for request {}: {}", request.getRequestURI(), firstErrorMessage);
        String userMessage = "The request data is invalid. Please check the fields. Details: " + firstErrorMessage;
        ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, userMessage, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles malformed JSON or missing request body.
     * Catches: 400 Bad Request.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRes> handleMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("🚫 Could not read request body for {}: {}", request.getRequestURI(), ex.getMessage());
        String userMessage = "Request body is missing or JSON format is invalid.";
        ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, userMessage, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles database constraint violations (e.g., unique key conflicts).
     * Catches: 409 Conflict.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRes> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        String rootCauseMessage = ex.getMostSpecificCause().getMessage();
        log.warn("⚠️ Data integrity violation for request {}: {}", request.getRequestURI(), rootCauseMessage);

        String userMessage = "The request violates a database constraint. Please check your input.";
        if (rootCauseMessage != null && rootCauseMessage.contains("client_profiles_email_key")) {
            userMessage = "This email is already registered.";
        }
        
        ErrorRes errorResponse = new ErrorRes(HttpStatus.CONFLICT, userMessage, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * The ultimate fallback handler for all other uncaught exceptions.
     * Catches: 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleAllUncaughtException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String path = request.getRequestURI();
        
        log.error("🔥🔥🔥 Uncaught Exception: {} {} - Root cause: {}", status.value(), path, ex.getMessage(), ex);

        String message;
        if ("dev".equalsIgnoreCase(activeProfile) || "local".equalsIgnoreCase(activeProfile)) {
            message = "An unexpected error occurred. Cause: " + ex.getMessage();
        } else {
            message = "An unexpected internal server error occurred. Please contact support or check server logs for details.";
        }

        ErrorRes errorResponse = new ErrorRes(status, message, path);
        return new ResponseEntity<>(errorResponse, status);
    }
}
