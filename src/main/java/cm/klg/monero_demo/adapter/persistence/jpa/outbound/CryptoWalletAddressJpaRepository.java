package cm.klg.monero_demo.adapter.persistence.jpa.outbound;

import cm.klg.monero_demo.application.outbound.CryptoWalletAddressRepository;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import cm.klg.monero_demo.domain.exception.ResourceAlreadyExistsException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
public class CryptoWalletAddressJpaRepository implements CryptoWalletAddressRepository {
  private final CryptoWalletAddressSpringRepository addressSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insert(CryptoWalletAddress cryptoWalletAddress) {
    try {
      addressSpringRepository.save(jpaMapper.fromCryptoWalletAddress(cryptoWalletAddress));
    } catch (DataIntegrityViolationException e) {
      throw new ResourceAlreadyExistsException(
          "A crypto wallet address with the same value already exists.", e);
    }
  }

  @Override
  public void update(CryptoWalletAddress cryptoWalletAddress) {
    addressSpringRepository
        .findById(cryptoWalletAddress.getId().value())
        .ifPresent(
            jpa -> {
              jpaMapper.fromCryptoWalletAddress(jpa, cryptoWalletAddress);
              addressSpringRepository.save(jpa);
            });
  }

  @Override
  public Optional<CryptoWalletAddress> loadByCryptoAddressValue(String value) {
    return addressSpringRepository.findByValue(value).map(jpaMapper::toCryptoWalletAddress);
  }
}
