package cm.klg.monero_demo.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cm.klg.monero_demo.application.outbound.CryptoWalletAddressRepository;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoCurrency;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddressId;
import cm.klg.monero_demo.domain.exception.MoneroRpcException;
import cm.klg.monero_demo.domain.exception.ResourceAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    when(moneroWalletClient.createSubAddress(any(CryptoWalletAddressId.class))).thenReturn(address);
    // On ne mocke plus cryptoWalletAddress car c'est un objet interne créé par le UseCase

    // When
    CreateCryptoWalletUseCase.CryptoWalletResponse response =
        createCryptoWalletUseCase.create(currency);

    // Then
    assertNotNull(response);
    assertEquals(address, response.walletAddressValue());
    assertEquals("XMR", response.currency());
    assertNotNull(response.id());

    ArgumentCaptor<CryptoWalletAddress> captor = ArgumentCaptor.forClass(CryptoWalletAddress.class);
    verify(moneroWalletClient, times(1)).createSubAddress(any(CryptoWalletAddressId.class));
    verify(cryptoWalletAddressRepository, times(1)).insert(captor.capture());

    CryptoWalletAddress savedAddress = captor.getValue();
    assertEquals(address, savedAddress.getValue().value());
    assertEquals(currency, savedAddress.getCurrency());
  }

  @Test
  void create_shouldThrowMoneroRpcException_whenClientFails() {
    // Given
    CryptoCurrency currency = CryptoCurrency.XMR;

    when(moneroWalletClient.createSubAddress(any(CryptoWalletAddressId.class)))
        .thenThrow(new MoneroRpcException("RPC call failed"));

    // When & Then
    assertThrows(MoneroRpcException.class, () -> createCryptoWalletUseCase.create(currency));
    verify(cryptoWalletAddressRepository, never()).insert(any(CryptoWalletAddress.class));
  }

  @Test
  void create_shouldThrowExceptionAndLog_whenRepositoryFails() {
    // Given
    CryptoCurrency currency = CryptoCurrency.XMR;
    String address = "some-monero-address";

    when(moneroWalletClient.createSubAddress(any(CryptoWalletAddressId.class))).thenReturn(address);
    doThrow(new ResourceAlreadyExistsException("Already exists"))
        .when(cryptoWalletAddressRepository)
        .insert(any(CryptoWalletAddress.class));

    // When & Then
    assertThrows(
        ResourceAlreadyExistsException.class, () -> createCryptoWalletUseCase.create(currency));

    verify(moneroWalletClient, times(1)).createSubAddress(any(CryptoWalletAddressId.class));
    verify(cryptoWalletAddressRepository, times(1)).insert(any(CryptoWalletAddress.class));
  }
}
