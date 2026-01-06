package cm.klg.monero_demo.adapter.persistence.jpa.outbound;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@SuperBuilder
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_crypto_wallet_address")
public class CryptoWalletAddressJpa {

  @Id
  @Column(name = "c_id")
  private UUID id;

  @NonNull
  @Column(name = "c_value")
  private String value;

  @Column(name = "c_type")
  private String type;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    CryptoWalletAddressJpa that = (CryptoWalletAddressJpa) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
