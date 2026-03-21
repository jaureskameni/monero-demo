package cm.klg.monero_demo.utils.data;

public record MoneroRequest(String jsonrpc, String id, String method, Object params) {}
