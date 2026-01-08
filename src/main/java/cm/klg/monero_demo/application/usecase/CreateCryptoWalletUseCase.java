package cm.klg.monero_demo.application.usecase;

import cm.klg.monero_demo.application.outbound.CryptoWalletAddressRepository;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoCurrency;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddressValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateCryptoWalletUseCase {

  private final MoneroWalletClient moneroWalletClient;
  private final CryptoWalletAddressRepository cryptoWalletAddressRepository;

  public CryptoWalletResponse create(CryptoCurrency cryptoCurrency) {

    String subAddress = moneroWalletClient.createSubAddress(cryptoCurrency.name());

    CryptoWalletAddress cryptoWalletAddress =
        CryptoWalletAddress.of(new CryptoWalletAddressValue(subAddress), cryptoCurrency);

    try {

      cryptoWalletAddressRepository.save(cryptoWalletAddress);

    } catch (Exception e) {

      log.error(
          "CRITICAL: Monero sub-address was created ('{}') but failed to save to the database. An"
              + " orphaned address now exists. Manual intervention may be required.",
          subAddress,
          e);

      throw e;
    }

    return new CryptoWalletResponse(cryptoWalletAddress.getId().value(), cryptoWalletAddress.getValue().value(), cryptoWalletAddress.getType().name());
  }

  public record CryptoWalletResponse(UUID id, String walletAddressValue, String currencyType){}
}
