package cm.klg.monero_demo.application.outbound;

import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;

public interface CryptoWalletAddressRepository {

  void save(CryptoWalletAddress value);
}
