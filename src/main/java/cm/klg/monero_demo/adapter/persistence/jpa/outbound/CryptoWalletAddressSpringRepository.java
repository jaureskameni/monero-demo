package cm.klg.monero_demo.adapter.persistence.jpa.outbound;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface CryptoWalletAddressSpringRepository
    extends CrudRepository<CryptoWalletAddressJpa, UUID> {}
