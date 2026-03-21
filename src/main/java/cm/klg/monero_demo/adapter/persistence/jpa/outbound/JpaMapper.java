package cm.klg.monero_demo.adapter.persistence.jpa.outbound;

import cm.klg.monero_demo.domain.cryptocurrency.CryptoCurrency;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddressId;
import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddressValue;
import cm.klg.monero_demo.domain.cryptocurrency.IncomingTransaction;
import cm.klg.monero_demo.domain.cryptocurrency.IncomingTransactionId;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface JpaMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "currency", source = "currency")
  @Mapping(target = "value", source = "value.value")
  @Mapping(target = "incomingTransactions", source = "incomingTransactions")
  CryptoWalletAddressJpa fromCryptoWalletAddress(CryptoWalletAddress cryptoWalletAddress);

  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "walletAddress", ignore = true)
  IncomingTransactionJpa fromIncomingTransaction(IncomingTransaction incomingTransaction);

  default CryptoWalletAddress toCryptoWalletAddress(CryptoWalletAddressJpa jpa) {
    if (jpa == null) return null;

    CryptoWalletAddress domain =
        new CryptoWalletAddress(
            new CryptoWalletAddressId(jpa.getId()),
            jpa.getValue() != null ? new CryptoWalletAddressValue(jpa.getValue()) : null,
            CryptoCurrency.valueOf(jpa.getCurrency()));

    if (jpa.getIncomingTransactions() != null) {
      domain.setIncomingTransactions(
          jpa.getIncomingTransactions().stream()
              .map(this::toIncomingTransaction)
              .collect(Collectors.toList()));
    }

    return domain;
  }

  default IncomingTransaction toIncomingTransaction(IncomingTransactionJpa jpa) {
    if (jpa == null) return null;
    return IncomingTransaction.reconstitute(
        new IncomingTransactionId(jpa.getId()), jpa.getTransactionId(), jpa.getAmount());
  }

  @AfterMapping
  default void linkIncomingTransactions(@MappingTarget CryptoWalletAddressJpa jpa) {
    if (jpa.getIncomingTransactions() != null) {
      jpa.getIncomingTransactions().forEach(tx -> tx.setWalletAddress(jpa));
    }
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "currency", source = "currency")
  @Mapping(target = "value", source = "value.value")
  @Mapping(target = "incomingTransactions", source = "incomingTransactions")
  void fromCryptoWalletAddress(
      @MappingTarget CryptoWalletAddressJpa cryptoWalletAddressJpa,
      CryptoWalletAddress cryptoWalletAddress);
}
