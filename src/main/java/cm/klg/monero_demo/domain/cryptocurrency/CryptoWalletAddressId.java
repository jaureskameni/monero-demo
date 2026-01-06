package cm.klg.monero_demo.domain.cryptocurrency;

import java.util.UUID;

public record CryptoWalletAddressId(UUID value) {

  public static CryptoWalletAddressId generate() {
    return new CryptoWalletAddressId(UUID.randomUUID());
  }
}
