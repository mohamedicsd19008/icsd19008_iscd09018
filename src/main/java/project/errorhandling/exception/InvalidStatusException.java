package project.errorhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String invalidStatus, String expectedStatuses, String objectName) {
        super(String.format("Invalid Status. Only %s with status %s can change to %s ", objectName, expectedStatuses, invalidStatus));
    }

    public InvalidStatusException(String objectName, String expectedStatus) {
        super(String.format("Invalid Status. Only %s with  %s status  can be updated",objectName,expectedStatus));
    }
}
