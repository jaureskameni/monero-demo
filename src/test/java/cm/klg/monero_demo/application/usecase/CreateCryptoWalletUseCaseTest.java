package cm.klg.monero_demo.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cm.klg.monero_demo.application.outbound.CryptoWalletAddressRepository;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoCurrency;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import cm.klg.monero_demo.domain.exception.MoneroRpcException;
import cm.klg.monero_demo.domain.exception.ResourceAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCryptoWalletUseCaseTest {

  @Mock private MoneroWalletClient moneroWalletClient;

  @Mock private CryptoWalletAddressRepository cryptoWalletAddressRepository;

  @InjectMocks private CreateCryptoWalletUseCase createCryptoWalletUseCase;

  @Test
  void create_shouldReturnResponse_whenSuccessful() {
    // Given
      CryptoCurrency currency = CryptoCurrency.XMR;
    String address = "some-monero-address";
    when(moneroWalletClient.createSubAddress(currency.name())).thenReturn(address);
    doNothing().when(cryptoWalletAddressRepository).save(any(CryptoWalletAddress.class));

    // When
    CreateCryptoWalletUseCase.CryptoWalletResponse response = createCryptoWalletUseCase.create(currency);

    // Then
    assertNotNull(response);
    assertEquals(address, response.walletAddressValue());
    assertEquals("XMR", response.currencyType());
    assertNotNull(response.id());
    verify(moneroWalletClient, times(1)).createSubAddress(currency.name());
    verify(cryptoWalletAddressRepository, times(1)).save(any(CryptoWalletAddress.class));
  }

  @Test
  void create_shouldThrowMoneroRpcException_whenClientFails() {
    // Given
      CryptoCurrency currency = CryptoCurrency.XMR;
    when(moneroWalletClient.createSubAddress(currency.name()))
        .thenThrow(new MoneroRpcException("RPC call failed"));

    // When & Then
    assertThrows(MoneroRpcException.class, () -> createCryptoWalletUseCase.create(currency));
    verify(cryptoWalletAddressRepository, never()).save(any(CryptoWalletAddress.class));
  }

  @Test
  void create_shouldThrowExceptionAndLog_whenRepositoryFails() {
    // Given
      CryptoCurrency currency = CryptoCurrency.XMR;
    String address = "some-monero-address";
    when(moneroWalletClient.createSubAddress(currency.name())).thenReturn(address);
    doThrow(new ResourceAlreadyExistsException("Already exists"))
        .when(cryptoWalletAddressRepository)
        .save(any(CryptoWalletAddress.class));

    // When & Then
    assertThrows(
        ResourceAlreadyExistsException.class, () -> createCryptoWalletUseCase.create(currency));

    verify(moneroWalletClient, times(1)).createSubAddress(currency.name());
    verify(cryptoWalletAddressRepository, times(1)).save(any(CryptoWalletAddress.class));
  }
}
