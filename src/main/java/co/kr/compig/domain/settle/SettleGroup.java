package co.kr.compig.domain.settle;

import co.kr.compig.common.embedded.Created;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@SequenceGenerator(
    name = "settle_group_seq_gen",
    sequenceName = "settle_group_seq",
    initialValue = 1,
    allocationSize = 1
)
public class SettleGroup {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settle_group_seq_gen")
  @Column(name = "settle_group_id")
  private Long id;

  /* =================================================================
   * Domain mapping
  ================================================================= */

  /* =================================================================
   * Default columns
  ================================================================= */
  @Embedded
  @Builder.Default
  private Created createdAndModified = new Created();
}
