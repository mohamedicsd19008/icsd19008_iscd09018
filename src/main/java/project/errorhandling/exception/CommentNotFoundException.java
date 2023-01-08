package project.errorhandling.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String id) {
        super(String.format("Comment %s not found", id));
    }
}
