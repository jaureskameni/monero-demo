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

  public CryptoWalletResponse create(CryptoCurrency currency) {

    // 1. On crée l'objet avec l'adresse à null
    CryptoWalletAddress cryptoWalletAddress = CryptoWalletAddress.of(currency);

    try {
      // 3. Appel au RPC Monero en utilisant l'ID comme label
      String subAddress = moneroWalletClient.createSubAddress(cryptoWalletAddress.getId());

      // 4. On met à jour l'objet avec la valeur retournée
      cryptoWalletAddress.addCryptoWalletValue(new CryptoWalletAddressValue(subAddress));

      // 5. On enregistre la mise à jour
      cryptoWalletAddressRepository.insert(cryptoWalletAddress);

      // 6. Construction de l'objet de retour
      return new CryptoWalletResponse(
          cryptoWalletAddress.getId().value(),
          cryptoWalletAddress.getValue().value(),
          cryptoWalletAddress.getCurrency().name());

    } catch (Exception e) {
      log.error(
          "Error creating Monero address for ID '{}'. Intent is saved in DB.",
          cryptoWalletAddress.getId().value(),
          e);
      throw e;
    }
  }

  public record CryptoWalletResponse(UUID id, String walletAddressValue, String currency) {}
}
