package cm.klg.monero_demo.utils.data;

import java.util.List;

public record GetTransfersResult(List<TransferInfo> in) {
  public record TransferInfo(
      String address,
      long amount,
      long confirmations,
      long height,
      String txid,
      long timestamp,
      boolean locked,
      SubaddressIndex subaddr_index,
      long suggested_confirmations_threshold) {}

  public record SubaddressIndex(
      int major, // account_index
      int minor // address_index
      ) {}
}
