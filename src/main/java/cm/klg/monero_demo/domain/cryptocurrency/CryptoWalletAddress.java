package cm.klg.monero_demo.domain.cryptocurrency;

import lombok.Getter;

@Getter
public class CryptoWalletAddress {

  private CryptoWalletAddressId id;
  private CryptoWalletAddressValue value;
  private CryptoCurrency type;

  public CryptoWalletAddress(
      CryptoWalletAddressId id, CryptoWalletAddressValue value, CryptoCurrency type) {
    this.id = id;
    this.value = value;
    this.type = type;
  }

  public static CryptoWalletAddress of(CryptoWalletAddressValue value, CryptoCurrency type) {
    return new CryptoWalletAddress(CryptoWalletAddressId.generate(), value, type);
  }
}
