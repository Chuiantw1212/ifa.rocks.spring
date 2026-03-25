package rocks.ifa.spring.infra.exception;

import rocks.ifa.spring.infra.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        // First, check for the more specific data integrity violation
        if (ex instanceof DataIntegrityViolationException) {
            String rootCauseMessage = ex.getMostSpecificCause().getMessage();
            log.warn("⚠️ Data integrity violation: {}", rootCauseMessage);

            if (rootCauseMessage != null && rootCauseMessage.contains("client_profiles_email_key")) {
                String errorMessage = "This email is already registered.";
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), errorMessage);
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }
            
            // For other integrity violations, return a generic bad request
            String errorMessage = "The request violates a database constraint.";
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // For all other data access exceptions, return 503
        log.error("❌ Database access exception: ", ex);
        String errorMessage = "The database service is temporarily unavailable. Please check the logs.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("⚠️ DTO validation failed: {}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        String errorMessage = "The request data format is incorrect or missing required fields.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("🚫 Could not read request body: {}", ex.getMessage());
        String errorMessage = "Request body is missing or JSON format is invalid.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("🔥 An unexpected internal server error occurred: ", ex);
        String errorMessage = "An unexpected internal server error occurred.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
