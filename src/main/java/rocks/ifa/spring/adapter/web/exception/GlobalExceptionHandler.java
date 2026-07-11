package rocks.ifa.spring.adapter.web.exception;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.exception.SysException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String BIZ_ERROR_CODE = "BIZ_ERROR";
    private static final String SYS_ERROR_CODE = "SYS_ERROR";
    private static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";

    /**
     * Handles business logic exceptions (BizException).
     * These are expected errors, like "User not found" or "Insufficient balance".
     * Returns HTTP 200 OK, but with success=false in the response body.
     */
    @ExceptionHandler(value = BizException.class)
    public Response handleBizException(BizException e) {
        log.warn("---BizException Handler---: Code: {}, Message: {}", e.getErrCode(), e.getMessage());
        return Response.buildFailure(e.getErrCode(), e.getMessage());
    }

    /**
     * Handles system-level exceptions (SysException).
     * These are unexpected technical errors, like "Database connection failed".
     * Returns HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(value = SysException.class)
    public ResponseEntity<Response> handleSysException(SysException e, HttpServletRequest request) {
        log.error("---SysException Handler---: Path: {}, Code: {}, Message: {}", request.getRequestURI(), e.getErrCode(), e.getMessage(), e);
        Response response = Response.buildFailure(e.getErrCode(), "System Error: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles DTO validation failures (@Valid).
     * Returns HTTP 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("---Validation Exception Handler---: {}", errorMsg);
        Response response = Response.buildFailure(VALIDATION_ERROR_CODE, errorMsg);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * The ultimate fallback handler for all other uncaught exceptions.
     * Treats any other exception as a system error.
     * Returns HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Response> handleAllUncaughtException(Exception e, HttpServletRequest request) {
        log.error("---AllUncaughtException Handler---: Path: {}, Message: {}", request.getRequestURI(), e.getMessage(), e);
        Response response = Response.buildFailure(SYS_ERROR_CODE, "An unexpected internal server error occurred.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
