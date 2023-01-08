package project.errorhandling.exception;

public class NewsNotFoundException extends RuntimeException {
    public NewsNotFoundException(String id) {
        super(String.format("News %s not found", id));
    }
}
