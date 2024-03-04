package co.kr.compig.domain.settle;

import co.kr.compig.api.settle.dto.SettleResponse;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.embedded.Created;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    name = "settle_seq_gen",
    sequenceName = "settle_seq",
    initialValue = 1,
    allocationSize = 1
)
public class Settle {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settle_seq_gen")
  @Column(name = "settle_id")
  private Long id;

  @Column
  private String element; // 요소명

  @Column
  private Integer amount; // 금액

  @Column
  private Long settleGroupId; // 간병금액정책 그룹

  @Column
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private UseYn useYn = UseYn.Y;

  /* =================================================================
  * Domain mapping
  ================================================================= */

  /* =================================================================
  * Relation method
  ================================================================= */
  public SettleResponse toSettleResponse(){
    return SettleResponse.builder()
        .settleId(this.id)
        .element(this.element)
        .amount(this.amount)
        .settleGroupId(this.settleGroupId)
        .useYn(this.useYn)
        .build();
  }

  /* =================================================================
  * Default columns
  ================================================================= */
  @Embedded
  @Builder.Default
  private Created createdAndModified = new Created();

  public void setUseYn() {
    this.useYn = UseYn.N;
  }
}
