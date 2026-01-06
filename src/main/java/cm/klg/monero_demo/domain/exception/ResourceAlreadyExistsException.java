package cm.klg.monero_demo.domain.exception;

public class ResourceAlreadyExistsException extends MoneroDemoException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
