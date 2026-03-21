package cm.klg.monero_demo.utils.data;

/**
 * Generic Monero RPC Response wrapper.
 *
 * @param <T> The type of the result object.
 */
public record MoneroResponse<T>(String id, String jsonrpc, T result, MoneroError error) {}
