package cm.klg.monero_demo.application.outbound;

public interface MoneroWalletClient {
  String createSubAddress(String label);
}
