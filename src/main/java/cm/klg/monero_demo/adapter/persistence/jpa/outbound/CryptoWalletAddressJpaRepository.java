package cm.klg.monero_demo.adapter.persistence.jpa.outbound;

import cm.klg.monero_demo.application.outbound.CryptoWalletAddressRepository;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import cm.klg.monero_demo.domain.exception.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
public class CryptoWalletAddressJpaRepository implements CryptoWalletAddressRepository {
  private final CryptoWalletAddressSpringRepository cryptoWalletAddressRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void save(CryptoWalletAddress cryptoWalletAddress) {
    try {
      cryptoWalletAddressRepository.save(jpaMapper.fromCryptoWalletAddress(cryptoWalletAddress));
    } catch (DataIntegrityViolationException e) {
      throw new ResourceAlreadyExistsException(
          "A crypto wallet address with the same value already exists.", e);
    }
  }
}
