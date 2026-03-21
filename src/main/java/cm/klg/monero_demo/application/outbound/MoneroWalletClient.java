package cm.klg.monero_demo.application.outbound;

import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddressId;
import cm.klg.monero_demo.utils.data.GetTransfersResult;
import java.util.List;

public interface MoneroWalletClient {
  String createSubAddress(CryptoWalletAddressId label);

  List<String> getAllAddresses();

  List<GetTransfersResult.TransferInfo> getAllIncomingTransfers();
}
