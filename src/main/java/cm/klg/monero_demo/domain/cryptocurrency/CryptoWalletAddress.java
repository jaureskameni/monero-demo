package cm.klg.monero_demo.domain.cryptocurrency;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class CryptoWalletAddress {

  private CryptoWalletAddressId id;
  private CryptoWalletAddressValue value;
  private CryptoCurrency currency;
  private List<IncomingTransaction> incomingTransactions;

  public CryptoWalletAddress(
      CryptoWalletAddressId id, @Nullable CryptoWalletAddressValue value, CryptoCurrency currency) {
    this.id = id;
    this.value = value;
    this.currency = currency;
    this.incomingTransactions = new ArrayList<>();
  }

  public static CryptoWalletAddress of(CryptoCurrency cryptoCurrency) {
    return new CryptoWalletAddress(CryptoWalletAddressId.generate(), null, cryptoCurrency);
  }

  public void addCryptoWalletValue(CryptoWalletAddressValue walletAddressValue) {
    this.value = walletAddressValue;
  }

  public void addIncomingTransaction(String txid, long amount) {
    // Éviter les doublons basés sur le TXID
    boolean exists =
        incomingTransactions.stream().anyMatch(tx -> tx.getTransactionId().equals(txid));

    if (!exists) {
      this.incomingTransactions.add(IncomingTransaction.of(txid, amount));
    }
  }
}
