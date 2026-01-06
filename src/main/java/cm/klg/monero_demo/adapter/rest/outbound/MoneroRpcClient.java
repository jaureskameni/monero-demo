package cm.klg.monero_demo.adapter.rest.outbound;

import static cm.klg.monero_demo.utils.Constants.ACCOUNT_INDEX;
import static cm.klg.monero_demo.utils.Constants.ADDRESS;
import static cm.klg.monero_demo.utils.Constants.ID;
import static cm.klg.monero_demo.utils.Constants.JSONRPC;
import static cm.klg.monero_demo.utils.Constants.JSONRPCVALUE;
import static cm.klg.monero_demo.utils.Constants.LABEL;
import static cm.klg.monero_demo.utils.Constants.METHOD;
import static cm.klg.monero_demo.utils.Constants.PARAMS;
import static cm.klg.monero_demo.utils.Constants.RESULT;

import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import cm.klg.monero_demo.domain.exception.MoneroRpcException;
import cm.klg.monero_demo.utils.RpcMethode;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@RequiredArgsConstructor
public class MoneroRpcClient implements MoneroWalletClient {

  private final RestClient restClient;

  @Override
  public String createSubAddress(String label) {
    Map<String, Object> request =
        Map.of(
            JSONRPC,
            JSONRPCVALUE,
            ID,
            "0",
            METHOD,
            RpcMethode.CREATE_ADDRESS.getMethodName(),
            PARAMS,
            Map.of(ACCOUNT_INDEX, 0, LABEL, label));

    try {
      ResponseEntity<JsonNode> responseEntity =
          restClient
              .post()
              .contentType(MediaType.APPLICATION_JSON)
              .body(request)
              .retrieve()
              .toEntity(JsonNode.class);

      if (!responseEntity.getStatusCode().is2xxSuccessful() || !responseEntity.hasBody()) {
        throw new MoneroRpcException(
            "Failed to create sub-address. HTTP status: " + responseEntity.getStatusCode());
      }

      JsonNode response = responseEntity.getBody();
      if (response.has("error")) {
        String errorMessage = response.get("error").get("message").asText();
        log.error("Monero RPC error: {}", errorMessage);
        throw new MoneroRpcException("Monero RPC error: " + errorMessage);
      }

      return response.get(RESULT).get(ADDRESS).asText();

    } catch (RestClientException e) {
      log.error("Error communicating with Monero Wallet RPC", e);
      throw new MoneroRpcException("Error communicating with Monero Wallet RPC", e);
    }
  }
}
