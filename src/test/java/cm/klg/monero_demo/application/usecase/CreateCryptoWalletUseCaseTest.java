package cm.klg.monero_demo.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cm.klg.monero_demo.application.outbound.CryptoWalletAddressRepository;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
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
    String name = "test-wallet";
    String address = "some-monero-address";
    when(moneroWalletClient.createSubAddress(name)).thenReturn(address);
    doNothing().when(cryptoWalletAddressRepository).save(any(CryptoWalletAddress.class));

    // When
    CreateCryptoWalletUseCase.Response response = createCryptoWalletUseCase.create(name);

    // Then
    assertNotNull(response);
    assertEquals(address, response.value());
    assertEquals("XMR", response.type());
    assertNotNull(response.id());
    verify(moneroWalletClient, times(1)).createSubAddress(name);
    verify(cryptoWalletAddressRepository, times(1)).save(any(CryptoWalletAddress.class));
  }

  @Test
  void create_shouldThrowMoneroRpcException_whenClientFails() {
    // Given
    String name = "test-wallet";
    when(moneroWalletClient.createSubAddress(name))
        .thenThrow(new MoneroRpcException("RPC call failed"));

    // When & Then
    assertThrows(MoneroRpcException.class, () -> createCryptoWalletUseCase.create(name));
    verify(cryptoWalletAddressRepository, never()).save(any(CryptoWalletAddress.class));
  }

  @Test
  void create_shouldThrowExceptionAndLog_whenRepositoryFails() {
    // Given
    String name = "test-wallet";
    String address = "some-monero-address";
    when(moneroWalletClient.createSubAddress(name)).thenReturn(address);
    doThrow(new ResourceAlreadyExistsException("Already exists"))
        .when(cryptoWalletAddressRepository)
        .save(any(CryptoWalletAddress.class));

    // When & Then
    assertThrows(
        ResourceAlreadyExistsException.class, () -> createCryptoWalletUseCase.create(name));

    // Verification of the critical log message for orphaned addresses would ideally be done here.
    // This typically requires a test-specific log appender.
    verify(moneroWalletClient, times(1)).createSubAddress(name);
    verify(cryptoWalletAddressRepository, times(1)).save(any(CryptoWalletAddress.class));
  }
}
