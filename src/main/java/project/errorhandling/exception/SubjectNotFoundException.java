package project.errorhandling.exception;

public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException(String id) {
        super(String.format("Subject %s not found", id));
    }
}
