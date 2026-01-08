package cm.klg.monero_demo.domain.exception;

public class MoneroRpcException extends MoneroDemoException {
  public MoneroRpcException(String message) {
    super(message);
  }

  public MoneroRpcException(String message, Throwable cause) {
    super(message, cause);
  }
}
