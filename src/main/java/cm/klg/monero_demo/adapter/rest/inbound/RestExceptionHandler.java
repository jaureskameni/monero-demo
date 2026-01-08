package cm.klg.monero_demo.adapter.rest.inbound;

import cm.klg.monero_demo.domain.exception.MoneroRpcException;
import cm.klg.monero_demo.domain.exception.ResourceAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(MoneroRpcException.class)
  ProblemDetail handleMoneroRpcException(MoneroRpcException e) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
    problemDetail.setTitle("Monero Service Unavailable");
    return problemDetail;
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  ProblemDetail handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    problemDetail.setTitle("Resource Already Exists");
    return problemDetail;
  }
}
