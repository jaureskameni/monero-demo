package cm.klg.monero_demo.domain.cryptocurrency;

public record CryptoWalletAddressValue(String value) {

  public static CryptoWalletAddressValue from(String value) {
    return new CryptoWalletAddressValue(value);
  }
}
