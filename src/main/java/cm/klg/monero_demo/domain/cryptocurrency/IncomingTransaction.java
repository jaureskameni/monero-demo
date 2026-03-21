package cm.klg.monero_demo.domain.cryptocurrency;

import lombok.Getter;

@Getter
public class IncomingTransaction {
  private final IncomingTransactionId id;
  private final String transactionId;
  private final long amount;

  public IncomingTransaction(IncomingTransactionId id, String transactionId, long amount) {
    this.id = id;
    this.transactionId = transactionId;
    this.amount = amount;
  }

  public static IncomingTransaction of(String transactionId, long amount) {
    return new IncomingTransaction(IncomingTransactionId.generate(), transactionId, amount);
  }

  public static IncomingTransaction reconstitute(
      IncomingTransactionId id, String transactionId, long amount) {
    return new IncomingTransaction(id, transactionId, amount);
  }
}
