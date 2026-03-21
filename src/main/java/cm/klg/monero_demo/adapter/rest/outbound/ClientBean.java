package cm.klg.monero_demo.adapter.rest.outbound;

import cm.klg.monero_demo.application.config.Configurations;
import cm.klg.monero_demo.application.outbound.MoneroWalletClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class ClientBean {
  private final Configurations configurations;

  @Bean
  public RestTemplate restTemplate() {
    log.info("=== Monero RPC Configuration ===");
    log.info("URL: {}", configurations.url());
    log.info("Auth enabled: {}", configurations.enabled());
    log.info("================================");

    HttpClient httpClient = HttpClientBuilder.create().disableContentCompression().build();

    HttpComponentsClientHttpRequestFactory factory =
        new HttpComponentsClientHttpRequestFactory(httpClient);

    // BufferingClientHttpRequestFactory force le calcul de Content-Length
    BufferingClientHttpRequestFactory bufferingFactory =
        new BufferingClientHttpRequestFactory(factory);

    return new RestTemplate(bufferingFactory);
  }

  @Bean
  public MoneroWalletClient moneroWalletPort(
      RestTemplate restTemplate, Configurations configurations, ObjectMapper objectMapper) {
    return new MoneroRpcClient(restTemplate, configurations, objectMapper);
  }
}
