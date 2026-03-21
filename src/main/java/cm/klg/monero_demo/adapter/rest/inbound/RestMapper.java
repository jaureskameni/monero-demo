package cm.klg.monero_demo.adapter.rest.inbound;

import cm.klg.generated.monero.dto.CryptoWalletAddressResponseDTO;
import cm.klg.monero_demo.application.usecase.CreateCryptoWalletUseCase.CryptoWalletResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  @Mapping(target = "type", source = "currency")
  @Mapping(target = "value", source = "walletAddressValue")
  CryptoWalletAddressResponseDTO toCryptoWalletAddressDTO(
      CryptoWalletResponse cryptoWalletResponse);
}
