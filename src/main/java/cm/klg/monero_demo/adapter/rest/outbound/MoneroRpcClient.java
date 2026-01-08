package cm.klg.monero_demo.adapter.rest.outbound;

import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import cm.klg.monero_demo.domain.exception.MoneroRpcException;
import cm.klg.monero_demo.utils.RpcMethode;
import cm.klg.monero_demo.utils.data.CreateSubaddressParams;
import cm.klg.monero_demo.utils.data.CreateSubaddressResult;
import cm.klg.monero_demo.utils.data.MoneroRequest;
import cm.klg.monero_demo.utils.data.MoneroResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
public class MoneroRpcClient implements MoneroWalletClient {

  private final RestClient restClient;
  private final ObjectMapper objectMapper;

  @Override
  public String createSubAddress(String label) throws MoneroRpcException {
    CreateSubaddressParams params = new CreateSubaddressParams(0, label);
    CreateSubaddressResult result =
        callRpc(RpcMethode.CREATE_ADDRESS.getMethodName(), params, CreateSubaddressResult.class);
    return result.address();
  }

  private <T> T callRpc(String method, Object params, Class<T> resultType)
      throws MoneroRpcException {
    MoneroRequest request = new MoneroRequest(method, params);

    MoneroResponse response = restClient.post().body(request).retrieve().body(MoneroResponse.class);

    if (response != null && response.error() != null) {
      throw new MoneroRpcException("Monero RPC Error: " + response.error().message());
    }
    if (response == null || response.result() == null) {
      throw new MoneroRpcException("Monero RPC Error: No response or result received");
    }

    return objectMapper.convertValue(response.result(), resultType);
  }
}
