package cm.klg.monero_demo.config;

import cm.klg.monero_demo.application.outbound.CryptoWalletAddressRepository;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import cm.klg.monero_demo.application.usecase.CreateCryptoWalletUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoneroBeans {

  @Bean
  public CreateCryptoWalletUseCase createCryptoWalletUseCase(
      MoneroWalletClient moneroWalletClient,
      CryptoWalletAddressRepository walletAddressRepository) {
    return new CreateCryptoWalletUseCase(moneroWalletClient, walletAddressRepository);
  }
}
