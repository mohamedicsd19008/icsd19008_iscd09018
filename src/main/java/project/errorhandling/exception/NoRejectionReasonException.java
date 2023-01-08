package project.errorhandling.exception;

public class NoRejectionReasonException extends RuntimeException {

    public NoRejectionReasonException() {
        super("Rejection Reason must be provided");
    }
}
