package project.errorhandling.exception;

public class IncorrectRoleException extends RuntimeException {

    public IncorrectRoleException(String role) {
        super(String.format("Not authorized to perform this action as %s", role));
    }
}
