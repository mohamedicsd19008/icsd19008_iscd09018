package project.errorhandling.exception;

public class NewsAlreadyExistsException extends RuntimeException {

    public NewsAlreadyExistsException(String title) {
        super(String.format("News with title %s already exists", title));
    }
}
