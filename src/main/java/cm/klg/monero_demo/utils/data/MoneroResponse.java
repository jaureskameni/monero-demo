package cm.klg.monero_demo.utils.data;

public record MoneroResponse(String id, String jsonrpc, Object result, MoneroError error) {}
