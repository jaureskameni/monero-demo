package cm.klg.monero_demo.domain.exception;

public class MoneroDemoException extends RuntimeException {
  public MoneroDemoException(String message) {
    super(message);
  }

  public MoneroDemoException(String message, Throwable cause) {
    super(message, cause);
  }
}
