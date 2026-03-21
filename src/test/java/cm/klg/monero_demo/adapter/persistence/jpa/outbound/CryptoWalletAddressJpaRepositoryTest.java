package cm.klg.monero_demo.adapter.persistence.jpa.outbound;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cm.klg.monero_demo.domain.cryptocurrency.CryptoCurrency;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import cm.klg.monero_demo.domain.exception.ResourceAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class CryptoWalletAddressJpaRepositoryTest {

  @Mock private CryptoWalletAddressSpringRepository cryptoWalletAddressSpringRepository;

  @Mock private JpaMapper jpaMapper;

  @InjectMocks private CryptoWalletAddressJpaRepository cryptoWalletAddressJpaRepository;

  @Test
  void insert_shouldCallSpringRepository_whenSuccessful() {
    // Given
    CryptoWalletAddress cryptoWalletAddress = CryptoWalletAddress.of(CryptoCurrency.XMR);
    CryptoWalletAddressJpa jpaEntity = new CryptoWalletAddressJpa();

    when(jpaMapper.fromCryptoWalletAddress(cryptoWalletAddress)).thenReturn(jpaEntity);
    when(cryptoWalletAddressSpringRepository.save(jpaEntity)).thenReturn(jpaEntity);

    // When
    cryptoWalletAddressJpaRepository.insert(cryptoWalletAddress);

    // Then
    verify(jpaMapper, times(1)).fromCryptoWalletAddress(cryptoWalletAddress);
    verify(cryptoWalletAddressSpringRepository, times(1)).save(jpaEntity);
  }

  @Test
  void insert_shouldThrowResourceAlreadyExistsException_whenConstraintViolated() {
    // Given
    CryptoWalletAddress cryptoWalletAddress = CryptoWalletAddress.of(CryptoCurrency.XMR);
    CryptoWalletAddressJpa jpaEntity = new CryptoWalletAddressJpa();

    when(jpaMapper.fromCryptoWalletAddress(cryptoWalletAddress)).thenReturn(jpaEntity);
    when(cryptoWalletAddressSpringRepository.save(jpaEntity))
        .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

    // When & Then
    assertThrows(
        ResourceAlreadyExistsException.class,
        () -> cryptoWalletAddressJpaRepository.insert(cryptoWalletAddress));

    verify(jpaMapper, times(1)).fromCryptoWalletAddress(cryptoWalletAddress);
    verify(cryptoWalletAddressSpringRepository, times(1)).save(jpaEntity);
  }
}
