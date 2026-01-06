package cm.klg.monero_demo.application.usecase;

import cm.klg.monero_demo.application.outbound.CryptoWalletAddressRepository;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoCurrency;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddressValue;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateCryptoWalletUseCase {

  private final MoneroWalletClient moneroWalletClient;
  private final CryptoWalletAddressRepository cryptoWalletAddressRepository;

  public Response create(String name) {
    String subAddress = moneroWalletClient.createSubAddress(name);
    CryptoWalletAddress cryptoWalletAddress =
        CryptoWalletAddress.of(new CryptoWalletAddressValue(subAddress), CryptoCurrency.XMR);
    try {
      cryptoWalletAddressRepository.save(cryptoWalletAddress);
    } catch (Exception e) {
      log.error(
          "CRITICAL: Monero sub-address was created ('{}') but failed to save to the database. An orphaned address now exists. Manual intervention may be required.",
          subAddress,
          e);
      throw e;
    }
    return new Response(
        cryptoWalletAddress.getId().value(), subAddress, cryptoWalletAddress.getType().name());
  }

  public record Response(UUID id, String value, String type) {}
}
