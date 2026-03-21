package cm.klg.monero_demo.application.usecase;

import cm.klg.monero_demo.application.outbound.CryptoWalletAddressRepository;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import cm.klg.monero_demo.utils.data.GetTransfersResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncIncomingTransactionsUseCase {

  private final MoneroWalletClient moneroWalletClient;
  private final CryptoWalletAddressRepository cryptoWalletAddressRepository;

  @Transactional
  public void sync() {
    log.info("Starting sync of incoming transactions for central wallet (account index 0)");

    // 1. Récupérer les transferts entrants déjà filtrés (10+ confirmations et déverrouillés)
    List<GetTransfersResult.TransferInfo> incomingTransfers =
        moneroWalletClient.getAllIncomingTransfers();

    for (GetTransfersResult.TransferInfo transfer : incomingTransfers) {
      // 2. Chercher l'agrégat correspondant à l'adresse du transfert
      cryptoWalletAddressRepository
          .loadByCryptoAddressValue(transfer.address())
          .ifPresentOrElse(
              walletAddress -> {

                // 3. Ajouter la transaction à l'agrégat
                walletAddress.addIncomingTransaction(transfer.txid(), transfer.amount());

                // 4. Sauvegarder l'agrégat complet
                cryptoWalletAddressRepository.update(walletAddress);

                log.info(
                    "Synced transaction {} for address {}", transfer.txid(), transfer.address());
              },
              () -> log.warn("Received transaction for unknown address: {}", transfer.address()));
    }
  }
}
