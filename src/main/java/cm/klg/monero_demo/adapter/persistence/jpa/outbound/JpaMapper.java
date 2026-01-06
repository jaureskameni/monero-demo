package cm.klg.monero_demo.adapter.persistence.jpa.outbound;

import cm.klg.monero_demo.domain.cryptocurrency.CryptoWalletAddress;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface JpaMapper {
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "type", source = "type")
  @Mapping(target = "value", source = "value.value")
  CryptoWalletAddressJpa fromCryptoWalletAddress(CryptoWalletAddress cryptoWalletAddress);
}
