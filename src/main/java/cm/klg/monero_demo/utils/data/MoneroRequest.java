package cm.klg.monero_demo.utils.data;

public record MoneroRequest(String jsonrpc, String id, String method, Object params) {
  public MoneroRequest(String method, Object params) {
    this("2.0", "0", method, params);
  }
}
