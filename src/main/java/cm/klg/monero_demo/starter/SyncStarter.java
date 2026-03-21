package cm.klg.monero_demo.starter;

import cm.klg.monero_demo.application.usecase.SyncIncomingTransactionsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncStarter implements CommandLineRunner {

  private final SyncIncomingTransactionsUseCase syncIncomingTransactionsUseCase;

  @Override
  public void run(String... args) {
    log.info("Application started. Triggering initial Monero transaction sync...");
    try {
      syncIncomingTransactionsUseCase.sync();
      log.info("Initial sync completed successfully.");
    } catch (Exception e) {
      log.error("Failed to perform initial Monero sync on startup.", e);
    }
  }
}
