package cm.klg.monero_demo.adapter.rest.outbound;

import cm.klg.monero_demo.application.config.Configurations;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddressId;
import cm.klg.monero_demo.domain.exception.MoneroRpcException;
import cm.klg.monero_demo.utils.Constants;
import cm.klg.monero_demo.utils.RpcMethode;
import cm.klg.monero_demo.utils.data.CreateSubaddressParams;
import cm.klg.monero_demo.utils.data.CreateSubaddressResult;
import cm.klg.monero_demo.utils.data.GetAddressParams;
import cm.klg.monero_demo.utils.data.GetAddressResult;
import cm.klg.monero_demo.utils.data.GetTransfersParams;
import cm.klg.monero_demo.utils.data.GetTransfersResult;
import cm.klg.monero_demo.utils.data.MoneroRequest;
import cm.klg.monero_demo.utils.data.MoneroResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
public class MoneroRpcClient implements MoneroWalletClient {

  private final RestTemplate restTemplate;
  private final Configurations configurations;
  private final ObjectMapper objectMapper;

  @Override
  public String createSubAddress(CryptoWalletAddressId label) throws MoneroRpcException {
    // Le 'label' sera notre UUID transformé en String
    CreateSubaddressParams params =
        new CreateSubaddressParams(configurations.accountIndex(), label.value().toString());
    CreateSubaddressResult result =
        callRpc(RpcMethode.CREATE_ADDRESS.getMethodName(), params, CreateSubaddressResult.class);
    return result.address();
  }

  @Override
  public List<String> getAllAddresses() {
    GetAddressParams params = new GetAddressParams(configurations.accountIndex());
    GetAddressResult result =
        callRpc(RpcMethode.GET_ADDRESS.getMethodName(), params, GetAddressResult.class);
    return result.addresses().stream()
        .map(GetAddressResult.MoneroAddressInfo::address)
        .collect(Collectors.toList());
  }

  @Override
  public List<GetTransfersResult.TransferInfo> getAllIncomingTransfers() {
    GetTransfersParams params =
        new GetTransfersParams(
            true, // in
            configurations.accountIndex(), // Récupéré depuis la configuration centralisée
            null // toutes les adresses
            );

    GetTransfersResult result =
        callRpc(RpcMethode.GET_TRANSFERS.getMethodName(), params, GetTransfersResult.class);

    if (result == null || result.in() == null) {
      return new ArrayList<>();
    }

    // FILTRE : MIN_CONFIRMATIONS+ confirmations ET non verrouillée (prête à l'emploi)
    return result.in().stream()
        .filter(
            transfer ->
                transfer.confirmations() >= Constants.MIN_CONFIRMATIONS && !transfer.locked())
        .collect(Collectors.toList());
  }

  private <T> T callRpc(String method, Object params, Class<T> resultType)
      throws MoneroRpcException {
    try {
      MoneroRequest request =
          new MoneroRequest(configurations.rpcVersion(), configurations.rpcId(), method, params);

      log.info(
          "=== Monero RPC Request === Method: {}, Params: {}",
          method,
          objectMapper.writeValueAsString(params));

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<MoneroRequest> entity = new HttpEntity<>(request, headers);

      // Utilisation d'un ParameterizedTypeReference anonyme pour désérialiser le type générique
      // Cela évite le double-mapping avec convertValue
      ParameterizedTypeReference<MoneroResponse<T>> responseType =
          new ParameterizedTypeReference<>() {
            @Override
            @NonNull
            public Type getType() {
              return objectMapper
                  .getTypeFactory()
                  .constructParametricType(MoneroResponse.class, resultType);
            }
          };

      MoneroResponse<T> response =
          Optional.ofNullable(
                  restTemplate
                      .exchange(configurations.url(), HttpMethod.POST, entity, responseType)
                      .getBody())
              .orElseThrow(() -> new MoneroRpcException("Monero RPC Error: No response received"));

      log.info(
          "=== Monero RPC Response === Result: {}",
          objectMapper.writeValueAsString(response.result()));

      if (response.error() != null) {
        throw new MoneroRpcException("Monero RPC Error: " + response.error().message());
      }

      if (response.result() == null) {
        throw new MoneroRpcException("Monero RPC Error: No result received from Monero Wallet RPC");
      }

      return response.result();
    } catch (MoneroRpcException e) {
      throw e;
    } catch (Exception e) {
      log.error("Error communicating with Monero Wallet RPC", e);
      throw new MoneroRpcException(
          "Error communicating with Monero Wallet RPC: " + e.getMessage(), e);
    }
  }
}
