package cm.klg.monero_demo.adapter.rest.inbound;

import cm.klg.generated.monero.dto.CryptoCurrencyTypeDTO;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoCurrency;
import org.springframework.stereotype.Component;

@Component
public class CryptoCurrencyMapper {

  public CryptoCurrency toCryptoCurrency(CryptoCurrencyTypeDTO cryptoCurrencyTypeDTO) {
    if (cryptoCurrencyTypeDTO == null) {
      return null;
    }
    try {
      return CryptoCurrency.valueOf(cryptoCurrencyTypeDTO.name());
    } catch (IllegalArgumentException e) {
      // Or throw a custom exception, e.g., MappingException
      throw new IllegalArgumentException(
          "Unsupported currency type: " + cryptoCurrencyTypeDTO.name(), e);
    }
  }
}
