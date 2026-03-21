package cm.klg.monero_demo.application.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cm.klg.monero_demo.application.outbound.CryptoWalletAddressRepository;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import cm.klg.monero_demo.utils.data.GetTransfersResult;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SyncIncomingTransactionsUseCaseTest {

  @Mock private MoneroWalletClient moneroWalletClient;

  @Mock private CryptoWalletAddressRepository cryptoWalletAddressRepository;

  @InjectMocks private SyncIncomingTransactionsUseCase syncIncomingTransactionsUseCase;

  @Test
  @DisplayName("Should sync valid incoming transactions")
  void shouldSyncValidIncomingTransactions() {
    // Given
    String txid = "tx123";
    long amount = 1000000L;
    String address = "addr123";

    GetTransfersResult.TransferInfo transfer =
        new GetTransfersResult.TransferInfo(
            address,
            amount,
            15, // > 10
            100L,
            txid,
            System.currentTimeMillis(),
            false, // not locked
            new GetTransfersResult.SubaddressIndex(0, 1),
            10L);

    when(moneroWalletClient.getAllIncomingTransfers()).thenReturn(List.of(transfer));

    CryptoWalletAddress walletAddress = mock(CryptoWalletAddress.class);
    when(cryptoWalletAddressRepository.loadByCryptoAddressValue(address))
        .thenReturn(Optional.of(walletAddress));

    // When
    syncIncomingTransactionsUseCase.sync();

    // Then
    verify(walletAddress).addIncomingTransaction(txid, amount);
    verify(cryptoWalletAddressRepository).update(walletAddress);
  }

  @Test
  @DisplayName("Should skip unknown address")
  void shouldSkipUnknownAddress() {
    // Given
    String address = "unknown_addr";
    GetTransfersResult.TransferInfo transfer =
        new GetTransfersResult.TransferInfo(
            address,
            1000L,
            15,
            100L,
            "tx123",
            System.currentTimeMillis(),
            false,
            new GetTransfersResult.SubaddressIndex(0, 1),
            10L);

    when(moneroWalletClient.getAllIncomingTransfers()).thenReturn(List.of(transfer));
    when(cryptoWalletAddressRepository.loadByCryptoAddressValue(address))
        .thenReturn(Optional.empty());

    // When
    syncIncomingTransactionsUseCase.sync();

    // Then
    verify(cryptoWalletAddressRepository, never()).update(any());
  }
}
