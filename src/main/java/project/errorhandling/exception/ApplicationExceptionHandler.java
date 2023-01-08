package project.errorhandling.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler({SubjectNotFoundException.class, NewsNotFoundException.class,CommentNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException ex) {
        return new ResponseEntity<>(getBody(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidStatusException.class, NewsAlreadyExistsException.class,NoRejectionReasonException.class})
    public ResponseEntity<Object> handleBadRequestsExceptions(RuntimeException ex) {
        return new ResponseEntity<>(getBody(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthException.class,IncorrectRoleException.class})
    public ResponseEntity<Object> handleForbiddenException(RuntimeException ex) {
        return new ResponseEntity<>(getBody(ex), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = "";
            if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
            } else if (error != null) {
                fieldName = error.getObjectName();
            }
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getBody(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return body;
    }

}
