package cm.klg.monero_demo.adapter.persistence.jpa.outbound;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface CryptoWalletAddressSpringRepository
    extends CrudRepository<CryptoWalletAddressJpa, UUID> {
  Optional<CryptoWalletAddressJpa> findByValue(String value);
}
