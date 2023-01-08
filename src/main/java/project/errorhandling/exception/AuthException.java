package project.errorhandling.exception;

public class AuthException extends RuntimeException {

    public AuthException() {
        super("Incorrect credentials");
    }
}
