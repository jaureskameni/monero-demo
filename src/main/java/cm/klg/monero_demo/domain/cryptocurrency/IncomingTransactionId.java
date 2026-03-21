package cm.klg.monero_demo.domain.cryptocurrency;

import java.util.UUID;

public record IncomingTransactionId(UUID value) {

  public static IncomingTransactionId generate() {
    return new IncomingTransactionId(UUID.randomUUID());
  }
}
