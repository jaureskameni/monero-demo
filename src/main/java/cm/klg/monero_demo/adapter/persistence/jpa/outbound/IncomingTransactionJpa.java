package cm.klg.monero_demo.adapter.persistence.jpa.outbound;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_incoming_transaction")
public class IncomingTransactionJpa {
  @Id
  @Column(name = "c_id")
  private UUID id;

  @Column(name = "c_transaction_id", nullable = false)
  private String transactionId;

  @Column(name = "c_amount", nullable = false)
  private long amount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_crypto_wallet_address_id")
  private CryptoWalletAddressJpa walletAddress;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    IncomingTransactionJpa that = (IncomingTransactionJpa) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
