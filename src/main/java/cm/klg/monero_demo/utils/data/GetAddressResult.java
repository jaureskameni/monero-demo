package cm.klg.monero_demo.utils.data;

import java.util.List;

public record GetAddressResult(List<MoneroAddressInfo> addresses) {
  public record MoneroAddressInfo(String address, String label, int address_index, boolean used) {}
}
