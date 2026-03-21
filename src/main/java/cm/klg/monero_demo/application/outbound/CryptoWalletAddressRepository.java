package cm.klg.monero_demo.application.outbound;

import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import java.util.Optional;

public interface CryptoWalletAddressRepository {
  void insert(CryptoWalletAddress value);

  void update(CryptoWalletAddress value);

  Optional<CryptoWalletAddress> loadByCryptoAddressValue(String value);
}
