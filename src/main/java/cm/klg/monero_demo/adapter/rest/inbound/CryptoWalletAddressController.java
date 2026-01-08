package cm.klg.monero_demo.adapter.rest.inbound;

import cm.klg.generated.monero.api.CryptoWalletAddressApi;
import cm.klg.generated.monero.dto.CryptoCurrencyTypeDTO;
import cm.klg.generated.monero.dto.CryptoWalletAddressResponseDTO;
import cm.klg.monero_demo.application.usecase.CreateCryptoWalletUseCase;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoCurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CryptoWalletAddressController implements CryptoWalletAddressApi {

  private final CreateCryptoWalletUseCase createCryptoWalletUseCase;
  private final RestMapper restMapper;
  private final CryptoCurrencyMapper cryptoCurrencyMapper;

  @Override
  public ResponseEntity<CryptoWalletAddressResponseDTO> createCryptoWalletAddress(
      CryptoCurrencyTypeDTO currency) {


    var wallet = createCryptoWalletUseCase.create(CryptoCurrency.valueOf(currency.name()));

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(restMapper.toCryptoWalletAddressDTO(wallet));
  }
}
