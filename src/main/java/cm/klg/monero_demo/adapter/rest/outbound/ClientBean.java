package cm.klg.monero_demo.adapter.rest.outbound;

import cm.klg.monero_demo.application.config.Configurations;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Configuration
public class ClientBean {
  private final Configurations configurations;

  @Bean
  public RestClient restClient() {
    String auth = configurations.username() + ":" + configurations.password();
    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    return RestClient.builder()
        .baseUrl(configurations.url())
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth)
        .build();
  }

  @Bean
  public MoneroWalletClient moneroWalletPort(RestClient restClient, ObjectMapper objectMapper) {
    return new MoneroRpcClient(restClient, objectMapper);
  }
}
